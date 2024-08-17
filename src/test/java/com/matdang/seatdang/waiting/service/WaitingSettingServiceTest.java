package com.matdang.seatdang.waiting.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.matdang.seatdang.store.entity.Store;
import com.matdang.seatdang.store.repository.StoreRepository;
import com.matdang.seatdang.store.repository.query.dto.AvailableWaitingTime;
import com.matdang.seatdang.store.vo.StoreSetting;
import com.matdang.seatdang.store.vo.WaitingTime;
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

    @Test
    @DisplayName("존재하는 이용 가능한 웨이팅 시간 가져오기")
    public void findAvailableWaitingTime() {
        // given
        storeRepository.save(Store.builder()
                .storeId(1L)
                .storeSetting(StoreSetting.builder()
                        .waitingTime(WaitingTime.builder()
                                .waitingOpenTime(LocalTime.of(9, 0))
                                .waitingCloseTime(LocalTime.of(22, 0))
                                .build())
                        .build())
                .build());
        // when
        AvailableWaitingTime findResult = waitingSettingService.findAvailableWaitingTime(1L);

        // then
        assertThat(findResult).isNotNull();
        assertThat(findResult.getWaitingOpenTime()).isEqualTo(LocalTime.of(9, 0));
        assertThat(findResult.getWaitingCloseTime()).isEqualTo(LocalTime.of(22, 0));
    }

    @Test
    @DisplayName("존재하지 않는 이용 가능한 웨이팅 시간 가져오기")
    public void findAvailableWaitingTimeByNotExistence() {
        // given
        storeRepository.save(Store.builder()
                .storeId(1L)
                .storeSetting(StoreSetting.builder()
                        .build())
                .build());
        // when
        AvailableWaitingTime findResult = waitingSettingService.findAvailableWaitingTime(1L);
        System.out.println("findResult = " + findResult);

        // then
        assertThat(findResult).isNotNull();
        assertThat(findResult.getWaitingOpenTime()).isEqualTo(LocalTime.of(0, 0));
        assertThat(findResult.getWaitingCloseTime()).isEqualTo(LocalTime.of(0, 0));
    }

}