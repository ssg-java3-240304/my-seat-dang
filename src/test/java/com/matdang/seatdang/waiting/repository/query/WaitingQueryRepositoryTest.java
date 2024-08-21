package com.matdang.seatdang.waiting.repository.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.matdang.seatdang.store.entity.Store;
import com.matdang.seatdang.store.repository.StoreRepository;
import com.matdang.seatdang.waiting.entity.CustomerInfo;
import com.matdang.seatdang.waiting.entity.Waiting;
import com.matdang.seatdang.waiting.entity.WaitingStatus;
import com.matdang.seatdang.waiting.entity.WaitingStorage;
import com.matdang.seatdang.waiting.repository.WaitingRepository;
import com.matdang.seatdang.waiting.repository.query.dto.WaitingDto;
import com.matdang.seatdang.waiting.repository.query.dto.WaitingInfoDto;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@Transactional
class WaitingQueryRepositoryTest {
    @Autowired
    private WaitingQueryRepository waitingQueryRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private WaitingRepository waitingRepository;
    @Autowired
    private EntityManager em;

    @BeforeEach
    void setUp() {
        {
            long i = 0;
            for (WaitingStatus value : WaitingStatus.values()) {
                for (int j = 0; j < 10; j++, i++) {
                    waitingRepository.save(Waiting.builder()
                            .waitingNumber(i)
                            .waitingOrder(i)
                            .storeId(1L)
                            .customerInfo(new CustomerInfo(i, "010-1111-1111", ((int) (Math.random() * 3 + 1))))
                            .waitingStatus(value)
                            .visitedTime(null)
                            .build());
                }
            }
        }

        for (long i = 0; i < 10; i++) {
            waitingRepository.save(Waiting.builder()
                    .waitingNumber(i)
                    .waitingOrder(i)
                    .storeId(2L)
                    .customerInfo(new CustomerInfo(i, "010-1111-1111", ((int) (Math.random() * 3 + 1))))
                    .waitingStatus(WaitingStatus.WAITING)
                    .visitedTime(null)
                    .build());
        }

        em.flush();
        em.clear();
    }

    @ParameterizedTest
    @CsvSource(value = {"1,WAITING,10", "2,WAITING,10", "1,VISITED,10", "2,VISITED,0", "1,NO_SHOW,10"})
    @DisplayName("상점 id로 특정 상태인 웨이팅 모두 조회 ")
    void findAllByStoreIdAndWaitingStatus(long shopId, String status, int size) {
        // given
        // when
        List<WaitingDto> findWaitings = waitingQueryRepository.findAllByStoreIdAndWaitingStatus(shopId,
                WaitingStatus.valueOf(status));

        // then
        assertThat(findWaitings.size()).isEqualTo(size);
    }

    @ParameterizedTest
    @CsvSource(value = {"1,30", "2,0"})
    @DisplayName("상점 id로 취소 상태(노쇼, 점주, 고객) 웨이팅 모두 조회")
    void findAllByCancelStatus(Long storeId, int size) {
        // given
        // when
        List<WaitingDto> findWaitings = waitingQueryRepository.findAllByCancelStatus(storeId);
        // then
        assertThat(findWaitings.size()).isEqualTo(size);
    }

    @Test
    @DisplayName("웨이팅 순서로 정렬된 웨이팅 상태 모두 조회")
    void findAllByWaitingStatusOrderByWaitingOrder() {
        // given
        for (long i = 0; i < 10; i++) {
            waitingRepository.save(Waiting.builder()
                    .waitingNumber(i)
                    .waitingOrder(10 - i)
                    .storeId(3L)
                    .customerInfo(new CustomerInfo(i, "010-1111-1111", ((int) (Math.random() * 3 + 1))))
                    .waitingStatus(WaitingStatus.WAITING)
                    .visitedTime(null)
                    .build());
        }
        // when
        List<WaitingDto> findWaitings = waitingQueryRepository.findAllByWaitingStatusOrderByWaitingOrder(3L);
        // then

        for (int i = 0; i < findWaitings.size(); i++) {
            assertThat(findWaitings.get(i).getWaitingOrder()).isEqualTo(i + 1);
        }
    }

    @Test
    @DisplayName("상점 id로 WaitingStorage entity형태로 웨이팅 가져오기")
    void findAllByStoreId() {
        // given
        // when
        List<WaitingStorage> findResult = waitingQueryRepository.findAllByStoreId(1L);
        System.out.println("findResult = " + findResult);

        // then
        assertThat(findResult.size()).isEqualTo(50);
    }

}