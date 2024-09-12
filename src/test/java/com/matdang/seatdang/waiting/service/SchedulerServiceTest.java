package com.matdang.seatdang.waiting.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.matdang.seatdang.auth.service.AuthService;
import com.matdang.seatdang.member.entity.Customer;
import com.matdang.seatdang.store.entity.Store;
import com.matdang.seatdang.store.repository.StoreRepository;
import com.matdang.seatdang.store.vo.StoreSetting;
import com.matdang.seatdang.store.vo.WaitingTime;
import com.matdang.seatdang.waiting.redis.Waiting;
import com.matdang.seatdang.waiting.entity.WaitingStorage;
import com.matdang.seatdang.waiting.repository.WaitingStorageRepository;
import com.matdang.seatdang.waiting.service.facade.RedissonLockWaitingCustomerFacade;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
class SchedulerServiceTest {
    @Autowired
    private SchedulerService schedulerService;
    @Autowired
    private WaitingStorageRepository waitingStorageRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private EntityManager em;
    @Autowired
    private RedissonLockWaitingCustomerFacade redissonLockWaitingCustomerFacade;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private RedisTemplate<String, Waiting> waitingRedisTemplate;
    @Autowired
    private WaitingService waitingService;
    @MockBean
    private AuthService authService;

    @Rollback(value = false)
    @Test
    @DisplayName("웨이팅 종료상태(마감, 이용불가)시 Redis 오늘 데이터 -> Mysql 기록 데이터 이동")
    void relocateWaitingData() {
        // given
        LocalDateTime start = LocalDateTime.now();
        Store storeA = storeRepository.save(Store.builder()
                .storeSetting(StoreSetting.builder()
                        .waitingStatus(com.matdang.seatdang.store.vo.WaitingStatus.CLOSE)
                        .build())
                .build());

        Store storeB = storeRepository.save(Store.builder()
                .storeSetting(StoreSetting.builder()
                        .waitingStatus(com.matdang.seatdang.store.vo.WaitingStatus.UNAVAILABLE)
                        .build())
                .build());

        Store storeC = storeRepository.save(Store.builder()
                .storeSetting(StoreSetting.builder()
                        .waitingStatus(com.matdang.seatdang.store.vo.WaitingStatus.OPEN)
                        .build())
                .build());

        em.flush();
        em.clear();

        Customer mockCustomer = Customer.builder()
                .memberId(1L)
                .memberPhone("010-1234-1234")
                .build();
        when(authService.getAuthenticatedMember()).thenReturn(mockCustomer);
        List<Long> waitingIds = new ArrayList<>();
        // storeA
        // 등록 50개
        for (int i = 0; i < 50; i++) {
            Long id = redissonLockWaitingCustomerFacade.createWaiting(storeA.getStoreId(), 2).getWaitingNumber();
            waitingIds.add(id);
        }
        // 취소 50개
        for (int i = 0; i < 50; i++) {
            Long id = redissonLockWaitingCustomerFacade.createWaiting(storeA.getStoreId(), 2).getWaitingNumber();
            redissonLockWaitingCustomerFacade.cancelWaitingByCustomer(id, storeA.getStoreId());
            waitingIds.add(id);
        }

        // storeB
        // 등록 10000개
        for (int i = 0; i < 10000; i++) {
            Long id = redissonLockWaitingCustomerFacade.createWaiting(storeB.getStoreId(), 2).getWaitingNumber();
            waitingIds.add(id);
        }

        // storeC
        // 등록 50개
        for (int i = 0; i < 50; i++) {
            Long id = redissonLockWaitingCustomerFacade.createWaiting(storeC.getStoreId(), 2).getWaitingNumber();
            waitingIds.add(id);
        }

        // when
        schedulerService.relocateWaitingData();

        // then
        // storeA

        HashOperations<String, Long, Waiting> hashOperations = waitingRedisTemplate.opsForHash();
        List<Waiting> waitingsA = hashOperations.values("store:" + storeA.getStoreId());

        assertThat(waitingsA.size()).isZero();

        String maxAo = (String) redisTemplate.opsForValue().get("waitingOrder:" + storeA.getStoreId());
        String maxAn = (String) redisTemplate.opsForValue().get("waitingNumber:" + storeA.getStoreId());
        assertThat(maxAo).isNull();
        assertThat(maxAn).isNull();


        // storeB
        List<Waiting> waitingsB = hashOperations.values("store:" + storeB.getStoreId());

        assertThat(waitingsB.size()).isZero();

        String maxBo = (String) redisTemplate.opsForValue().get("waitingOrder:" + storeB.getStoreId());
        String maxBn = (String) redisTemplate.opsForValue().get("waitingNumber:" + storeB.getStoreId());
        assertThat(maxBo).isNull();
        assertThat(maxBn).isNull();

        // storeC
        List<Waiting> waitingsC = hashOperations.values("store:" + storeC.getStoreId());

        assertThat(waitingsC.size()).isEqualTo(50);

        String maxCo = (String) redisTemplate.opsForValue().get("waitingOrder:" + storeC.getStoreId());
        String maxCn = (String) redisTemplate.opsForValue().get("waitingNumber:" + storeC.getStoreId());
        assertThat(Integer.parseInt(maxCo)).isEqualTo(50);
        assertThat(Integer.parseInt(maxCn)).isEqualTo(50);

        List<WaitingStorage> findSA = waitingStorageRepository.findAllByStoreId(storeA.getStoreId());
        List<WaitingStorage> findSB = waitingStorageRepository.findAllByStoreId(storeB.getStoreId());
        List<WaitingStorage> findSC = waitingStorageRepository.findAllByStoreId(storeC.getStoreId());

        // then
        assertThat(findSA.size()).isEqualTo(100);
        assertThat(findSB.size()).isEqualTo(10000);
        assertThat(findSC.size()).isEqualTo(0);

        LocalDateTime end = LocalDateTime.now();
        System.out.println("  =====  elapsed time  ===== ");
        System.out.println("  =====  "+Duration.between(start, end).toMillis() +"  =====  ");


        // 시퀀스 PooledOptimizer 적용 16818, 17008 ms
        // Auto 19514, 20438 -> seq 테이블이 생성되어 batch가 적용됨
        // Identity 28934, 29484 -> batch 적용 안됨
    }

    @Test
    @DisplayName("웨이팅 마감 시간일 때, 마감 상태로 변경")
    void closeWaiting() {
        // given
        Store storeA = storeRepository.save(Store.builder()
                .storeSetting(StoreSetting.builder()
                        .waitingStatus(com.matdang.seatdang.store.vo.WaitingStatus.CLOSE)
                        .build())
                .build());

        Store storeB = storeRepository.save(Store.builder()
                .storeSetting(StoreSetting.builder()
                        .waitingStatus(com.matdang.seatdang.store.vo.WaitingStatus.UNAVAILABLE)
                        .build())
                .build());

        Store storeC = storeRepository.save(Store.builder()
                .storeSetting(StoreSetting.builder()
                        .waitingStatus(com.matdang.seatdang.store.vo.WaitingStatus.OPEN)
                        .waitingTime(WaitingTime.builder()
                                .waitingCloseTime(LocalTime.now())
                                .build())
                        .build())
                .build());

        Store storeD = storeRepository.save(Store.builder()
                .storeSetting(StoreSetting.builder()
                        .waitingStatus(com.matdang.seatdang.store.vo.WaitingStatus.OPEN)
                        .waitingTime(WaitingTime.builder()
                                .waitingCloseTime(LocalTime.now().plusMinutes(10))
                                .build())
                        .build())
                .build());

        Store storeE = storeRepository.save(Store.builder()
                .storeSetting(StoreSetting.builder()
                        .waitingStatus(com.matdang.seatdang.store.vo.WaitingStatus.OPEN)
                        .waitingTime(WaitingTime.builder()
                                .waitingCloseTime(LocalTime.now().minusMinutes(10))
                                .build())
                        .build())
                .build());

        // when
        em.flush();
        em.clear();

        schedulerService.closeWaiting();
        em.flush();
        em.clear();

        Store findA = storeRepository.findByStoreId(storeA.getStoreId());
        Store findB = storeRepository.findByStoreId(storeB.getStoreId());
        Store findC = storeRepository.findByStoreId(storeC.getStoreId());
        Store findD = storeRepository.findByStoreId(storeD.getStoreId());
        Store findE = storeRepository.findByStoreId(storeE.getStoreId());

        // then
        assertThat(findA.getStoreSetting().getWaitingStatus()).isEqualTo(
                com.matdang.seatdang.store.vo.WaitingStatus.CLOSE);
        assertThat(findB.getStoreSetting().getWaitingStatus()).isEqualTo(
                com.matdang.seatdang.store.vo.WaitingStatus.UNAVAILABLE);

        assertThat(findC.getStoreSetting().getWaitingStatus()).isEqualTo(
                com.matdang.seatdang.store.vo.WaitingStatus.CLOSE);
        assertThat(findD.getStoreSetting().getWaitingStatus()).isEqualTo(
                com.matdang.seatdang.store.vo.WaitingStatus.OPEN);
        assertThat(findE.getStoreSetting().getWaitingStatus()).isEqualTo(
                com.matdang.seatdang.store.vo.WaitingStatus.CLOSE);

    }

    @Test
    @DisplayName("웨이팅 마감 시간이 자정 이상일 때, 마감 상태로 변경")
    void closeWaitingByMidnight() {
        // 현재 시간에 따라 테스트가 실패할 수 있음
        // given
        Store storeA = storeRepository.save(Store.builder()
                .storeSetting(StoreSetting.builder()
                        .waitingStatus(com.matdang.seatdang.store.vo.WaitingStatus.OPEN)
                        .waitingTime(WaitingTime.builder()
                                .waitingCloseTime(LocalTime.of(0, 0))

                                .build())
                        .build())
                .build());

        Store storeB = storeRepository.save(Store.builder()
                .storeSetting(StoreSetting.builder()
                        .waitingStatus(com.matdang.seatdang.store.vo.WaitingStatus.OPEN)
                        .waitingTime(WaitingTime.builder()

                                .waitingCloseTime(LocalTime.of(11,50))
                                .build())
                        .build())
                .build());

        Store storeC = storeRepository.save(Store.builder()
                .storeSetting(StoreSetting.builder()
                        .waitingStatus(com.matdang.seatdang.store.vo.WaitingStatus.OPEN)
                        .waitingTime(WaitingTime.builder()

                                .waitingCloseTime(LocalTime.of(12,0))
                                .build())
                        .build())
                .build());

        Store storeD = storeRepository.save(Store.builder()
                .storeSetting(StoreSetting.builder()
                        .waitingStatus(com.matdang.seatdang.store.vo.WaitingStatus.OPEN)
                        .waitingTime(WaitingTime.builder()

                                .waitingCloseTime(LocalTime.of(23,50))
                                .build())
                        .build())
                .build());

        Store storeE = storeRepository.save(Store.builder()
                .storeSetting(StoreSetting.builder()
                        .waitingStatus(com.matdang.seatdang.store.vo.WaitingStatus.OPEN)
                        .waitingTime(WaitingTime.builder()
                                .waitingCloseTime(LocalTime.now().minusMinutes(10))
                                .build())
                        .build())
                .build());

        // when
        em.flush();
        em.clear();

        schedulerService.closeWaiting();
        em.flush();
        em.clear();

        Store findA = storeRepository.findByStoreId(storeA.getStoreId());
        Store findB = storeRepository.findByStoreId(storeB.getStoreId());
        Store findC = storeRepository.findByStoreId(storeC.getStoreId());
        Store findD = storeRepository.findByStoreId(storeD.getStoreId());
        Store findE = storeRepository.findByStoreId(storeE.getStoreId());

        // then
        assertThat(findA.getStoreSetting().getWaitingStatus()).isEqualTo(
                com.matdang.seatdang.store.vo.WaitingStatus.OPEN);
        assertThat(findB.getStoreSetting().getWaitingStatus()).isEqualTo(
                com.matdang.seatdang.store.vo.WaitingStatus.OPEN);

        assertThat(findC.getStoreSetting().getWaitingStatus()).isEqualTo(
                com.matdang.seatdang.store.vo.WaitingStatus.CLOSE);
        assertThat(findD.getStoreSetting().getWaitingStatus()).isEqualTo(
                com.matdang.seatdang.store.vo.WaitingStatus.OPEN);
        assertThat(findE.getStoreSetting().getWaitingStatus()).isEqualTo(
                com.matdang.seatdang.store.vo.WaitingStatus.CLOSE);

    }


    @Test
    @DisplayName("시간 로직 테스트")
    void timeLogic() {
        // given
        long minutes = Duration.between(LocalTime.now(), LocalTime.of(0, 0))
                .toMinutes();

        LocalTime current = LocalTime.of(23, 0);
        LocalTime setting = LocalTime.of(0, 0);
        LocalTime minus = LocalTime.of(23, 50);

        assertThat(minutes).isNegative();
        assertThat(setting.isAfter(current)).isFalse();

        assertThat(minus.minusHours(12)).isEqualTo(11);

        // when
        // then
    }
}