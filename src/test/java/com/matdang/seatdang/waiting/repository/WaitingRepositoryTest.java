package com.matdang.seatdang.waiting.repository;

import com.matdang.seatdang.waiting.repository.query.WaitingQueryRepository;
import com.matdang.seatdang.waiting.repository.query.dto.WaitingDto;
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
import org.springframework.data.domain.PageRequest;
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
    private WaitingQueryRepository waitingQueryRepository;
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
    @CsvSource(value = {"1,50", "2,10"})
    @DisplayName("상점 id로 웨이팅 전체 조회")
    void findAllByStoreId(long shopId, int size) {
        // given
        // when
        List<Waiting> findWaitings = waitingRepository.findAllByStoreId(shopId);

        // then
        assertThat(findWaitings.size()).isEqualTo(size);
    }

    @Test
    @DisplayName("id로 입장 상태 변경")
    void updateStatusByVisit() {
        // given
        List<Waiting> findWaiting = waitingRepository.findAll();
        em.clear();

        // when
        int result = waitingRepository.updateStatusByVisit(findWaiting.get(1).getId());
        // then
        assertThat(result).isEqualTo(1);
        assertThat(waitingRepository.findById(findWaiting.get(1).getId()).get().getWaitingStatus())
                .isEqualTo(WaitingStatus.VISITED);
    }

    @Test
    @DisplayName("id로 점주 취소 상태 변경")
    void updateStatusByShopCancel() {
        // given
        List<Waiting> findWaiting = waitingRepository.findAll();
        em.clear();

        // when
        int result = waitingRepository.updateStatusByShopCancel(findWaiting.get(1).getId());
        // then
        assertThat(result).isEqualTo(1);
        assertThat(waitingRepository.findById(findWaiting.get(1).getId()).get().getWaitingStatus())
                .isEqualTo(WaitingStatus.SHOP_CANCELED);
    }

    @Test
    @DisplayName("상점 id로 웨이팅 중인 모든 웨이팅 번호 1 감소")
    void updateAllWaitingNumberByVisit() {
        // given
        List<Waiting> findWaiting = waitingRepository.findAll();
        em.clear();
        // when
        int result = waitingRepository.updateAllWaitingNumberByVisit(1L);
        // then
        assertThat(result).isEqualTo(10);
        assertThat(waitingRepository.findById(findWaiting.get(0).getId()).get().getWaitingOrder()).isEqualTo(
                -1L);
    }

    @Test
    @DisplayName("웨이팅 취소시, 해당 웨이팅 번호 이후로 웨이팅 번호 1 감소")
    void updateWaitingNumberByCancel() {
        // given
        // when
        int result = waitingRepository.updateWaitingNumberByCancel(1L, 5L);
        // then
        assertThat(result).isEqualTo(4);
    }

    @Test
    @DisplayName("상점 id로 웨이팅 전체 취소")
    void cancelAllWaiting() {
        // given
        PageRequest pageable = PageRequest.of(0, 10);

        // when
        int result = waitingRepository.cancelAllWaiting(1L);
        // then
        assertThat(result).isEqualTo(10);
        assertThat(waitingQueryRepository.findAllByStoreIdAndWaitingStatus(1L, WaitingStatus.WAITING, pageable)
                .getTotalElements())
                .isEqualTo(0);
        assertThat(waitingQueryRepository.findAllByStoreIdAndWaitingStatus(1L, WaitingStatus.SHOP_CANCELED, pageable)
                .getTotalElements()).isEqualTo(20);
    }

    @Test
    @DisplayName("상점 id로 웨이팅 전체 삭제")
    void deleteAllByStoreId() {
        // given
        // when
        int result = waitingRepository.deleteAllByStoreId(1L);
        List<Waiting> findResult = waitingRepository.findAllByStoreId(1L);
        // then
        assertThat(result).isEqualTo(50);
        assertThat(findResult.size()).isEqualTo(0);
    }


}