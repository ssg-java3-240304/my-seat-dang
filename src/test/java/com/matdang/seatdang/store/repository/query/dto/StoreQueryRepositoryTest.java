package com.matdang.seatdang.store.repository.query.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.matdang.seatdang.store.entity.Store;
import com.matdang.seatdang.store.repository.StoreRepository;
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
class StoreQueryRepositoryTest {
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private StoreQueryRepository storeQueryRepository;

    @Test
    @DisplayName("이용가능한 웨이팅 시간 가져오기")
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

        AvailableWaitingTime findResult = storeQueryRepository.findAvailableWaitingTime(1L);

        // then
        assertThat(findResult).isNotNull();
        assertThat(findResult.getWaitingOpenTime()).isEqualTo(LocalTime.of(9, 0));
        assertThat(findResult.getWaitingCloseTime()).isEqualTo(LocalTime.of(22, 0));
    }

    @Test
    @DisplayName("웨이팅 예상 대기 시간 가져오기")
    public void findEstimatedWaitingTime() {
        // given
        storeRepository.save(Store.builder()
                .storeId(1L)
                .storeSetting(StoreSetting.builder()
                        .waitingTime(WaitingTime.builder()
                                .estimatedWaitingTime(LocalTime.of(0,20))
                                .build())
                        .build())
                .build());
        // when

        LocalTime findResult = storeQueryRepository.findEstimatedWaitingTime(1L);

        // then
        assertThat(findResult).isNotNull();
        assertThat(findResult).isEqualTo(LocalTime.of(0, 20));
    }

}