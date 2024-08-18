package com.matdang.seatdang.waiting.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.matdang.seatdang.waiting.entity.CustomerInfo;
import com.matdang.seatdang.waiting.entity.Waiting;
import com.matdang.seatdang.waiting.entity.WaitingStatus;
import com.matdang.seatdang.waiting.entity.WaitingStorage;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class WaitingStorageRepositoryTest {

    @Autowired
    private WaitingStorageRepository waitingStorageRepository;
    @Autowired
    private EntityManager em;

    @ParameterizedTest
    @CsvSource(value = {"1,50", "2,10"})
    @DisplayName("상점 id로 웨이팅 가져오기")
    void findAllByStoreId(long shopId, int size) {
        // given
        {
            long i = 0;
            for (WaitingStatus value : WaitingStatus.values()) {
                for (int j = 0; j < 10; j++, i++) {
                    waitingStorageRepository.save(WaitingStorage.builder()
                            .waitingNumber(i)
                            .waitingOrder(i)
                            .storeId(1L)
                            .customerInfo(new CustomerInfo(i, "010-1111-1111", ((long) (Math.random() * 3 + 1))))
                            .waitingStatus(value)
                            .visitedTime(null)
                            .build());
                }
            }
        }

        for (long i = 0; i < 10; i++) {
            waitingStorageRepository.save(WaitingStorage.builder()
                    .waitingNumber(i)
                    .waitingOrder(i)
                    .storeId(2L)
                    .customerInfo(new CustomerInfo(i, "010-1111-1111", ((long) (Math.random() * 3 + 1))))
                    .waitingStatus(WaitingStatus.WAITING)
                    .visitedTime(null)
                    .build());
        }

        em.flush();
        em.clear();
        // when
        List<WaitingStorage> findWaitings = waitingStorageRepository.findAllByStoreId(shopId);

        // then
        assertThat(findWaitings.size()).isEqualTo(size);
    }

}