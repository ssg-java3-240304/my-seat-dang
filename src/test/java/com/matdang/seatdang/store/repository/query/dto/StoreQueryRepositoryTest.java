package com.matdang.seatdang.store.repository.query.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.matdang.seatdang.store.entity.Store;
import com.matdang.seatdang.store.repository.StoreRepository;
import com.matdang.seatdang.store.vo.StoreSetting;
import com.matdang.seatdang.store.vo.WaitingStatus;
import com.matdang.seatdang.store.vo.WaitingTime;
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
class StoreQueryRepositoryTest {
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private StoreQueryRepository storeQueryRepository;
    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("이용가능한 웨이팅 시간 가져오기")
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

        AvailableWaitingTime findResult = storeQueryRepository.findAvailableWaitingTime(store.getStoreId());

        // then
        assertThat(findResult).isNotNull();
        assertThat(findResult.getWaitingOpenTime()).isEqualTo(LocalTime.of(9, 0));
        assertThat(findResult.getWaitingCloseTime()).isEqualTo(LocalTime.of(22, 0));
    }

    @Test
    @DisplayName("웨이팅 예상 대기 시간 가져오기")
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

        LocalTime findResult = storeQueryRepository.findEstimatedWaitingTime(store.getStoreId());

        // then
        assertThat(findResult).isNotNull();
        assertThat(findResult).isEqualTo(LocalTime.of(0, 20));
    }

    @ParameterizedTest
    @ValueSource(strings = {"OPEN", "CLOSE","UNAVAILABLE"})
    @DisplayName("웨이팅 상태 가져오기")
    public void findWaitingStatus(String waitingStatus) {
        // given
        Store store = storeRepository.save(Store.builder()
                .storeSetting(StoreSetting.builder()
                        .waitingStatus(WaitingStatus.valueOf(waitingStatus))
                        .build())
                .build());
        em.flush();
        em.clear();
        // when

        WaitingStatus findResult = storeQueryRepository.findWaitingStatus(store.getStoreId());

        // then
        assertThat(findResult).isNotNull();
        assertThat(findResult).isEqualTo(WaitingStatus.valueOf(waitingStatus));
    }
}