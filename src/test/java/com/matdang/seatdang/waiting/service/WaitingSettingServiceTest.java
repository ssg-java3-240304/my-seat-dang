package com.matdang.seatdang.waiting.service;

import com.matdang.seatdang.auth.service.AuthService;
import com.matdang.seatdang.member.entity.Customer;
import com.matdang.seatdang.store.entity.Store;
import com.matdang.seatdang.store.repository.StoreRepository;
import com.matdang.seatdang.store.repository.query.dto.AvailableWaitingTime;
import com.matdang.seatdang.store.vo.StoreSetting;
import com.matdang.seatdang.store.vo.WaitingTime;
import com.matdang.seatdang.waiting.redis.Waiting;
import com.matdang.seatdang.waiting.entity.WaitingStatus;

import com.matdang.seatdang.waiting.service.facade.RedissonLockWaitingCustomerFacade;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
class WaitingSettingServiceTest {
    @Autowired
    private RedissonLockWaitingCustomerFacade redissonLockWaitingCustomerFacade;
    @MockBean
    private AuthService authService;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private WaitingSettingService waitingSettingService;
    @Autowired
    private EntityManager em;
    @Autowired
    private WaitingService waitingService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private RedisTemplate<String, Waiting> waitingRedisTemplate;

    @Test
    @DisplayName("존재하는 이용 가능한 웨이팅 시간 가져오기")
    public void findAvailableWaitingTime() {
        // given
        Store store = storeRepository.save(Store.builder()
                .storeSetting(StoreSetting.builder()
                        .waitingTime(WaitingTime.builder()
                                .waitingOpenTime(LocalTime.of(9, 0))
                                .waitingCloseTime(LocalTime.of(22, 0))
                                .build())
                        .build())
                .build());
        em.flush();
        em.clear();

        // when
        AvailableWaitingTime findResult = waitingSettingService.findAvailableWaitingTime(store.getStoreId());

        // then
        assertThat(findResult).isNotNull();
        assertThat(findResult.getWaitingOpenTime()).isEqualTo(LocalTime.of(9, 0));
        assertThat(findResult.getWaitingCloseTime()).isEqualTo(LocalTime.of(22, 0));
    }

    @Test
    @DisplayName("존재하지 않는 이용 가능한 웨이팅 시간 가져오기")
    public void findAvailableWaitingTimeByNotExistence() {
        // given
        Store store = storeRepository.save(Store.builder()
                .storeSetting(StoreSetting.builder()
                        .build())
                .build());
        em.flush();
        em.clear();

        // when
        AvailableWaitingTime findResult = waitingSettingService.findAvailableWaitingTime(store.getStoreId());

        // then
        assertThat(findResult).isNotNull();
        assertThat(findResult.getWaitingOpenTime()).isEqualTo(LocalTime.of(0, 0));
        assertThat(findResult.getWaitingCloseTime()).isEqualTo(LocalTime.of(0, 0));
    }

    @Test
    @DisplayName("존재하는 웨이팅 예상대기 시간 가져오기")
    public void findEstimatedWaitingTime() {
        // given
        Store store = storeRepository.save(Store.builder()
                .storeSetting(StoreSetting.builder()
                        .waitingTime(WaitingTime.builder()
                                .estimatedWaitingTime(LocalTime.of(0, 20))
                                .build())
                        .build())
                .build());
        em.flush();
        em.clear();

        // when
        LocalTime findResult = waitingSettingService.findEstimatedWaitingTime(store.getStoreId());

        // then
        assertThat(findResult).isNotNull();
        assertThat(findResult).isEqualTo(LocalTime.of(0, 20));
    }

    @Test
    @DisplayName("존재하지 않는 웨이팅 예상대기 시간 가져오기")
    public void findEstimatedWaitingTimeNotExistence() {
        // given
        Store store = storeRepository.save(Store.builder()
                .storeSetting(StoreSetting.builder()
                        .waitingTime(WaitingTime.builder()
                                .build())
                        .build())
                .build());
        em.flush();
        em.clear();
        // when

        LocalTime findResult = waitingSettingService.findEstimatedWaitingTime(store.getStoreId());

        // then
        assertThat(findResult).isNotNull();
        assertThat(findResult).isEqualTo(LocalTime.of(0, 0));
    }

    @Test
    @DisplayName("웨이팅 접수상태로 변경")
    public void changeWaitingStatusByOpen() {
        // given
        Store store = storeRepository.save(Store.builder()
                .storeId(1L)
                .storeSetting(StoreSetting.builder()
                        .waitingTime(WaitingTime.builder()
                                .build())
                        .build())
                .build());
        em.flush();
        em.clear();
        // when
        waitingSettingService.changeWaitingStatus(1, store.getStoreId());

        // then
        assertThat(storeRepository.findByStoreId(store.getStoreId()).getStoreSetting().getWaitingStatus()).isEqualTo(
                com.matdang.seatdang.store.vo.WaitingStatus.OPEN);
    }

    @Test
    @DisplayName("웨이팅 마감 상태로 변경")
    public void changeWaitingStatusByClose() {
        // given
        Store store = storeRepository.save(Store.builder()
                .storeId(1L)
                .storeSetting(StoreSetting.builder()
                        .waitingTime(WaitingTime.builder()
                                .build())
                        .build())
                .build());
        em.flush();
        em.clear();
        // when
        waitingSettingService.changeWaitingStatus(2, store.getStoreId());

        // then
        assertThat(storeRepository.findByStoreId(store.getStoreId()).getStoreSetting().getWaitingStatus()).isEqualTo(
                com.matdang.seatdang.store.vo.WaitingStatus.CLOSE);
    }

    @Test
    @DisplayName("웨이팅 불가 상태로 변경 - Redis 적용")
    public void changeWaitingStatusByUnavailable() {
        // given
        Store store = storeRepository.save(Store.builder()
                .storeId(1L)
                .storeSetting(StoreSetting.builder()
                        .waitingTime(WaitingTime.builder()
                                .build())
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
        for (int i = 0; i < 100; i++) {
            Long id = redissonLockWaitingCustomerFacade.createWaiting(1L, 2).getWaitingNumber();
            waitingIds.add(id);
        }

        // when
        waitingSettingService.changeWaitingStatus(3, store.getStoreId());

        // then
        assertThat(storeRepository.findByStoreId(store.getStoreId()).getStoreSetting().getWaitingStatus()).isEqualTo(
                com.matdang.seatdang.store.vo.WaitingStatus.UNAVAILABLE);


        String max = (String) redisTemplate.opsForValue().get("waitingOrder:1");
        assertThat(Integer.parseInt(max)).isEqualTo(0);


        HashOperations<String, Long, Waiting> hashOperations = waitingRedisTemplate.opsForHash();
        List<Waiting> waitingList = hashOperations.values("store:1");
        int waitingCount = 0;
        int canceledCount = 0;
        for (Waiting waiting : waitingList) {
            if (waiting.getWaitingStatus() == WaitingStatus.WAITING) {
                waitingCount++;
            }
            if (waiting.getWaitingStatus() == WaitingStatus.SHOP_CANCELED) {
                canceledCount++;
            }
        }

        assertThat(waitingCount).isEqualTo(0);
        assertThat(canceledCount).isEqualTo(100);
    }
//
//    @Test
//    @DisplayName("존재하는 웨이팅 인원수 가져오기")
//    public void findWaitingPeopleCount() {
//        // given
//        Store store = storeRepository.save(Store.builder()
//                .storeSetting(StoreSetting.builder()
//                        .waitingPeopleCount(3)
//                        .build())
//                .build());
//        em.flush();
//        em.clear();
//
//        // when
//        Integer findResult = waitingSettingService.findWaitingPeopleCount(store.getStoreId());
//
//        // then
//        assertThat(findResult).isNotNull();
//        assertThat(findResult).isEqualTo(3);
//    }
//
//    @Test
//    @DisplayName("존재하지 않는 웨이팅 인원수 가져오기")
//    public void findWaitingPeopleCountByNotExistence() {
//        // given
//        Store store = storeRepository.save(Store.builder()
//                .build());
//        em.flush();
//        em.clear();
//
//        // when
//        Integer findResult = waitingSettingService.findWaitingPeopleCount(store.getStoreId());
//
//        // then
//        assertThat(findResult).isNotNull();
//        assertThat(findResult).isEqualTo(0);
//    }
}