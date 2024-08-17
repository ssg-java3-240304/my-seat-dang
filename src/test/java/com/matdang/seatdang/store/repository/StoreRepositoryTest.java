package com.matdang.seatdang.store.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.matdang.seatdang.store.entity.Store;
import com.matdang.seatdang.store.vo.Status;
import com.matdang.seatdang.store.vo.StoreSetting;
import com.matdang.seatdang.store.vo.WaitingStatus;
import jakarta.persistence.EntityManager;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class StoreRepositoryTest {
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("웨이팅 이용가능한 시간 설정")
    void updateWaitingAvailableTime() {
        // given
        Store store = storeRepository.save(Store.builder()
                .build());
        em.clear();

        // when
        int result = storeRepository.updateWaitingAvailableTime(LocalTime.of(9, 0),
                LocalTime.of(22, 0), store.getStoreId());

        // then
        assertThat(result).isEqualTo(1);
        assertThat(storeRepository.findByStoreId(store.getStoreId()).getStoreSetting().getWaitingTime()
                .getWaitingOpenTime()).isEqualTo(LocalTime.of(9, 0));
        assertThat(storeRepository.findByStoreId(store.getStoreId()).getStoreSetting().getWaitingTime()
                .getWaitingCloseTime()).isEqualTo(LocalTime.of(22, 0));

    }

    @Test
    @DisplayName("웨이팅 예상 대기시간 설정")
    void updateEstimatedWaitingTime() {
        // given
        Store store = storeRepository.save(Store.builder()
                .storeId(1L)
                .build());
        em.clear();

        // when
        int result = storeRepository.updateEstimatedWaitingTime(LocalTime.of(0, 20), store.getStoreId());

        // then
        assertThat(result).isEqualTo(1);
        assertThat(storeRepository.findByStoreId(store.getStoreId()).getStoreSetting().getWaitingTime()
                .getEstimatedWaitingTime()).isEqualTo(LocalTime.of(0, 20));
    }

    @ParameterizedTest
    @ValueSource(strings = {"OPEN", "CLOSE", "UNAVAILABLE"})
    @DisplayName("상점 웨이팅 상태 업데이트")
    void updateWaitingStatus(String status) {
        // given
        Store store = storeRepository.save(Store.builder()
                .build());
        em.clear();

        // when
        int result = storeRepository.updateWaitingStatus(WaitingStatus.valueOf(status), store.getStoreId());

        // then
        assertThat(result).isEqualTo(1);
        assertThat(storeRepository.findByStoreId(store.getStoreId()).getStoreSetting().getWaitingStatus()).isEqualTo(
                WaitingStatus.valueOf(status));
    }

    @Test
    @DisplayName("웨이팅 상태 기본값 UNAVAILABLE")
    void setDefaultWaitingStatus() {
        Store store = storeRepository.save(Store.builder()
                .storeSetting(new StoreSetting())
                .build());
        em.clear();

        Store findStore = storeRepository.findByStoreId(store.getStoreId());
        System.out.println("findStore = " + findStore);

        assertThat(findStore.getStoreSetting().getWaitingStatus()).isEqualTo(WaitingStatus.UNAVAILABLE);
    }
}