package com.matdang.seatdang.store.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.matdang.seatdang.store.entity.Store;
import com.matdang.seatdang.store.vo.Status;
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
        storeRepository.save(Store.builder()
                .storeId(1L)
                .build());
        em.clear();

        // when
        int result = storeRepository.updateWaitingAvailableTime(LocalTime.of(9, 0),
                LocalTime.of(22, 0), 1L);

        // then
        assertThat(result).isEqualTo(1);
        assertThat(storeRepository.findByStoreId(1L).getStoreSetting().getWaitingTime()
                .getWaitingOpenTime()).isEqualTo(LocalTime.of(9, 0));
        assertThat(storeRepository.findByStoreId(1L).getStoreSetting().getWaitingTime()
                .getWaitingCloseTime()).isEqualTo(LocalTime.of(22, 0));

    }

    @Test
    @DisplayName("웨이팅 예상 대기시간 설정")
    void updateEstimatedWaitingTime() {
        // given
        storeRepository.save(Store.builder()
                .storeId(1L)
                .build());
        em.clear();

        // when
        int result = storeRepository.updateEstimatedWaitingTime(LocalTime.of(0, 20), 1L);

        // then
        assertThat(result).isEqualTo(1);
        assertThat(storeRepository.findByStoreId(1L).getStoreSetting().getWaitingTime()
                .getEstimatedWaitingTime()).isEqualTo(LocalTime.of(0, 20));
    }

    @ParameterizedTest
    @ValueSource(strings = {"ON", "OFF"})
    @DisplayName("상점 웨이팅 상태 업데이트")
    void updateWaitingStatus(String status) {
        // given
        storeRepository.save(Store.builder()
                .storeId(1L)
                .build());
        em.clear();

        // when
        int result = storeRepository.updateWaitingStatus(Status.valueOf(status), 1L);

        // then
        assertThat(result).isEqualTo(1);
        assertThat(storeRepository.findByStoreId(1L).getStoreSetting().getWaitingStatus()).isEqualTo(Status.valueOf(status));
    }
}