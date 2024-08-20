package com.matdang.seatdang.waiting.repository;

import com.matdang.seatdang.store.entity.Store;
import com.matdang.seatdang.store.repository.StoreRepository;
import com.matdang.seatdang.waiting.entity.CustomerInfo;
import com.matdang.seatdang.waiting.entity.Waiting;
import com.matdang.seatdang.waiting.entity.WaitingStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class WaitingCustomerTest {
    @Autowired
    private WaitingRepository waitingRepository;
    @Autowired
    private StoreRepository storeRepository;

    @Test
    @DisplayName("상점 id로 존재하는 웨이팅 번호 최대값 가져오기")
    void findMaxWaitingNumberByStoreId() {
        // given
        Store storeA = storeRepository.save(Store.builder()
                .build());
        for (long i = 0; i < 5; i++) {
            waitingRepository.save(Waiting.builder()
                    .waitingNumber(i)
                    .waitingOrder(i)
                    .storeId(storeA.getStoreId())
                    .customerInfo(new CustomerInfo(i, "010-1111-1111", ((long) (Math.random() * 3 + 1))))
                    .waitingStatus(WaitingStatus.WAITING)
                    .visitedTime(null)
                    .build());
        }
        for (long i = 5; i < 10; i++) {
            waitingRepository.save(Waiting.builder()
                    .waitingNumber(i)
                    .waitingOrder(i)
                    .storeId(storeA.getStoreId())
                    .customerInfo(new CustomerInfo(i, "010-1111-1111", ((long) (Math.random() * 3 + 1))))
                    .waitingStatus(WaitingStatus.VISITED)
                    .visitedTime(null)
                    .build());
        }

        // when
        Long findResult = waitingRepository.findMaxWaitingNumberByStoreId(storeA.getStoreId());

        // then
        assertThat(findResult).isEqualTo(9L);
    }

    @Test
    @DisplayName("상점 id의 웨이팅(전체 상태)이 존재하지 않을때 웨이팅 번호 0으로 가져오기")
    void findMaxWaitingNumberByStoreIdAndNotExistence() {
        // given
        Store storeA = storeRepository.save(Store.builder()
                .build());

        // when
        Long findResult = waitingRepository.findMaxWaitingNumberByStoreId(storeA.getStoreId());

        // then
        assertThat(findResult).isEqualTo(0L);
    }

    @Test
    @DisplayName("상점 id로 존재하는 웨이팅 상태의 웨이팅 순서 최대값 가져오기")
    void findMaxWaitingOrderByStoreId() {
        // given
        Store storeA = storeRepository.save(Store.builder()
                .build());
        for (long i = 0; i < 5; i++) {
            waitingRepository.save(Waiting.builder()
                    .waitingNumber(i)
                    .waitingOrder(i)
                    .storeId(storeA.getStoreId())
                    .customerInfo(new CustomerInfo(i, "010-1111-1111", ((long) (Math.random() * 3 + 1))))
                    .waitingStatus(WaitingStatus.WAITING)
                    .visitedTime(null)
                    .build());
        }
        for (long i = 5; i < 10; i++) {
            waitingRepository.save(Waiting.builder()
                    .waitingNumber(i)
                    .waitingOrder(i)
                    .storeId(storeA.getStoreId())
                    .customerInfo(new CustomerInfo(i, "010-1111-1111", ((long) (Math.random() * 3 + 1))))
                    .waitingStatus(WaitingStatus.VISITED)
                    .visitedTime(null)
                    .build());
        }

        // when
        Long findResult = waitingRepository.findMaxWaitingOrderByStoreId(storeA.getStoreId());

        // then
        assertThat(findResult).isEqualTo(4L);
    }
}
