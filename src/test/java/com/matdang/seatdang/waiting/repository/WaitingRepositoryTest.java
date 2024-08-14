package com.matdang.seatdang.waiting.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.matdang.seatdang.waiting.entity.CustomerInfo;
import com.matdang.seatdang.waiting.entity.Waiting;
import com.matdang.seatdang.waiting.entity.WaitingStatus;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@Transactional
class WaitingRepositoryTest {
    @Autowired
    private WaitingRepository waitingRepository;

    @Autowired
    private EntityManager em;

    @BeforeEach
    void setUp()  {

        for (long i = 0; i < 10; i++) {
            waitingRepository.save(Waiting.builder()
                    .waitingId(i)
                    .waitingNumber(i)
                    .storeId(1L)
                    .customerInfo(new CustomerInfo(i, "010-1111-1111"))
                    .waitingStatus(WaitingStatus.WAITING)
                    .createdAt(LocalDateTime.now())
                    .visitedTime(null)
                    .build());

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

        for (long i = 10; i < 20; i++) {
            waitingRepository.save(Waiting.builder()
                    .waitingId(i)
                    .waitingNumber(i)
                    .storeId(1L)
                    .customerInfo(new CustomerInfo(i, "010-1111-1111"))
                    .waitingStatus(WaitingStatus.VISITED)
                    .createdAt(LocalDateTime.now())
                    .visitedTime(null)
                    .build());

            waitingRepository.save(Waiting.builder()
                    .waitingId(i)
                    .waitingNumber(i)
                    .storeId(1L)
                    .customerInfo(new CustomerInfo(i, "010-1111-1111"))
                    .waitingStatus(WaitingStatus.NO_SHOW)
                    .createdAt(LocalDateTime.now())
                    .visitedTime(null)
                    .build());
        }
        em.flush();

    }

    @ParameterizedTest
    @CsvSource(value = {"1,30","2,10"})
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
}