package com.matdang.seatdang.store.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.matdang.seatdang.store.entity.Store;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class StoreRepositoryTest {
    @Autowired
    private StoreRepository storeRepository;

    @Test
    @DisplayName("웨이팅 이용가능한 시간 설정")
    void updateWaitingAvailableTime() {
        // given
        storeRepository.save(Store.builder()
                .storeId(1L)
                .build());
        // when
        int result = storeRepository.updateWaitingAvailableTime(LocalTime.of(9, 0),
                LocalTime.of(22, 0), 1L);

        // then
        assertThat(result).isEqualTo(1);

    }
}