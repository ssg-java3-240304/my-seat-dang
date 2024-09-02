package com.matdang.seatdang.waiting.service;

import com.matdang.seatdang.waiting.dto.UpdateRequest;
import com.matdang.seatdang.waiting.repository.query.dto.WaitingDto;
import com.matdang.seatdang.waiting.entity.CustomerInfo;
import com.matdang.seatdang.waiting.redis.Waiting;
import com.matdang.seatdang.waiting.entity.WaitingStatus;
import com.matdang.seatdang.waiting.repository.WaitingRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class WaitingServiceTest {
    @Autowired
    private WaitingRepository waitingRepository;
    @Autowired
    private WaitingService waitingService;

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
                    .customerInfo(new CustomerInfo(i, "010-1111-1111",((int) (Math.random() * 3 + 1))))
                    .waitingStatus(WaitingStatus.WAITING)
                    .visitedTime(null)
                    .build());
        }

        em.flush();
        em.clear();
    }

    @ParameterizedTest
    @CsvSource(value = {"1,1,10", "2,1,0", "1,2,30"})
    @DisplayName("웨이팅 상태별 조회")
    void showWaiting(long storeId, int status, int size) {
        // given
        // when
        Page<WaitingDto> waitings = waitingService.showWaiting(storeId, status, 0);
        // then
        assertThat(waitings.getTotalElements()).isEqualTo(size);
    }

    @ParameterizedTest
    @CsvSource(value = {"1,1,VISITED", "2,1,SHOP_CANCELED"})
    @DisplayName("웨이팅 상태 업데이트")
    void updateStatus(int status, Long shopId, WaitingStatus waitingStatus) {
        // given
        List<Waiting> findWaitings = waitingRepository.findAll();
        em.clear();

        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.setChangeStatus(status);
        updateRequest.setStoreId(shopId);
        updateRequest.setWaitingNumber(findWaitings.get(0).getId());
        updateRequest.setWaitingOrder(1L);

        // when
        waitingService.updateStatus(updateRequest);

        // then
        assertThat(waitingRepository.findById(findWaitings.get(0).getId()).get().getWaitingStatus())
                .isEqualTo(waitingStatus);
    }

}