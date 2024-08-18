package com.matdang.seatdang.waiting.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.matdang.seatdang.store.entity.Store;
import com.matdang.seatdang.store.repository.StoreRepository;
import com.matdang.seatdang.store.repository.query.dto.AvailableWaitingTime;
import com.matdang.seatdang.store.vo.StoreSetting;
import com.matdang.seatdang.store.vo.WaitingTime;
import com.matdang.seatdang.waiting.entity.CustomerInfo;
import com.matdang.seatdang.waiting.entity.Waiting;
import com.matdang.seatdang.waiting.entity.WaitingStatus;
import com.matdang.seatdang.waiting.repository.WaitingRepository;
import com.matdang.seatdang.waiting.repository.query.WaitingQueryRepository;
import jakarta.persistence.EntityManager;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class WaitingSettingServiceTest {

    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private WaitingSettingService waitingSettingService;
    @Autowired
    private WaitingRepository waitingRepository;
    @Autowired
    private WaitingQueryRepository waitingQueryRepository;
    @Autowired
    private EntityManager em;

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
    @DisplayName("웨이팅 불가 상태로 변경")
    public void changeWaitingStatusByUnavailable() {
        // given
        Store store = storeRepository.save(Store.builder()
                .storeId(1L)
                .storeSetting(StoreSetting.builder()
                        .waitingTime(WaitingTime.builder()
                                .build())
                        .build())
                .build());

        for (long i = 0; i < 10; i++) {
            waitingRepository.save(Waiting.builder()
                    .waitingNumber(i)
                    .waitingOrder(i)
                    .storeId(store.getStoreId())
                    .customerInfo(new CustomerInfo(i, "010-1111-1111", ((long) (Math.random() * 3 + 1))))
                    .waitingStatus(WaitingStatus.WAITING)
                    .visitedTime(null)
                    .build());
        }
        em.flush();
        em.clear();

        // when
        waitingSettingService.changeWaitingStatus(3, store.getStoreId());

        // then
        assertThat(storeRepository.findByStoreId(store.getStoreId()).getStoreSetting().getWaitingStatus()).isEqualTo(
                com.matdang.seatdang.store.vo.WaitingStatus.UNAVAILABLE);
        assertThat(waitingQueryRepository.findAllByStoreIdAndWaitingStatus(store.getStoreId(), WaitingStatus.WAITING)
                .size())
                .isEqualTo(0);
        assertThat(
                waitingQueryRepository.findAllByStoreIdAndWaitingStatus(store.getStoreId(), WaitingStatus.SHOP_CANCELED)
                        .size())
                .isEqualTo(10);
    }

    @Test
    @DisplayName("존재하는 웨이팅 인원수 가져오기")
    public void findWaitingPeopleCount() {
        // given
        Store store = storeRepository.save(Store.builder()
                .storeSetting(StoreSetting.builder()
                        .waitingPeopleCount(3)
                        .build())
                .build());
        em.flush();
        em.clear();

        // when
        Integer findResult = waitingSettingService.findWaitingPeopleCount(store.getStoreId());

        // then
        assertThat(findResult).isNotNull();
        assertThat(findResult).isEqualTo(3);
    }

    @Test
    @DisplayName("존재하지 않는 웨이팅 인원수 가져오기")
    public void findWaitingPeopleCountByNotExistence() {
        // given
        Store store = storeRepository.save(Store.builder()
                .build());
        em.flush();
        em.clear();

        // when
        Integer findResult = waitingSettingService.findWaitingPeopleCount(store.getStoreId());

        // then
        assertThat(findResult).isNotNull();
        assertThat(findResult).isEqualTo(0);
    }
}