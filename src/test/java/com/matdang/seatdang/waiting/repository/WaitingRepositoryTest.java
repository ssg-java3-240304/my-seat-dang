package com.matdang.seatdang.waiting.repository;

import com.matdang.seatdang.waiting.entity.CustomerInfo;
import com.matdang.seatdang.waiting.entity.Waiting;
import com.matdang.seatdang.waiting.entity.WaitingStatus;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class WaitingRepositoryTest {
    @Autowired
    private WaitingRepository waitingRepository;

    @Autowired
    private EntityManager em;

    @BeforeEach
    void setUp()  {
        {
            long i = 0;
            for (WaitingStatus value : WaitingStatus.values()) {
                for (int j = 0; j < 10; j++, i++) {
                    waitingRepository.save(Waiting.builder()
                            .waitingId(i)
                            .waitingNumber(i)
                            .storeId(1L)
                            .customerInfo(new CustomerInfo(i, "010-1111-1111"))
                            .waitingStatus(value)
                            .createdAt(LocalDateTime.now())
                            .visitedTime(null)
                            .build());

                }
            }
        }

        for (long i = 0; i < 10; i++) {
            waitingRepository.save(Waiting.builder()
                    .waitingId(i)
                    .waitingNumber(i)
                    .storeId(2L)
                    .customerInfo(new CustomerInfo(i, "010-1111-1111"))
                    .waitingStatus(WaitingStatus.WAITING)
                    .createdAt(LocalDateTime.now())
                    .visitedTime(null)
                    .build());
        }

        em.flush();
        em.clear();
    }

    @ParameterizedTest
    @CsvSource(value = {"1,50","2,10"})
    @DisplayName("상점 id로 웨이팅 전체 조회")
    void findAllByStoreId(long shopId, int size) {
        // given
        // when
        List<Waiting> findWaitings = waitingRepository.findAllByStoreId(shopId);

        // then
        assertThat(findWaitings.size()).isEqualTo(size);
    }

    @ParameterizedTest
    @CsvSource(value = {"1,WAITING,10","2,WAITING,10", "1,VISITED,10", "2,VISITED,0", "1,NO_SHOW,10"})
    @DisplayName("상점 id로 특정 상태인 웨이팅 모두 조회 ")
    void findAllByStoreIdAndWaitingStatus(long shopId, String status, int size) {
        // given
        // when
        List<Waiting> findWaitings = waitingRepository.findAllByStoreIdAndWaitingStatus(shopId, WaitingStatus.valueOf(status));

        // then
        assertThat(findWaitings.size()).isEqualTo(size);
    }

    @ParameterizedTest
    @CsvSource(value = {"1,30","2,0"})
    @DisplayName("상점 id로 취소 상태(노쇼, 점주, 고객) 웨이팅 모두 조회")
    void findAllByCancelStatus(Long storeId, int size) {
        // given
        // when
        List<Waiting> findWaitings = waitingRepository.findAllByCancelStatus(storeId);
        // then
        assertThat(findWaitings.size()).isEqualTo(size);
    }
}