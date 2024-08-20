package com.matdang.seatdang.waiting.repository;

import com.matdang.seatdang.store.entity.Store;
import com.matdang.seatdang.store.repository.StoreRepository;
import com.matdang.seatdang.waiting.entity.CustomerInfo;
import com.matdang.seatdang.waiting.entity.Waiting;
import com.matdang.seatdang.waiting.entity.WaitingStatus;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class WaitingCustomerTest {
    @Autowired
    private WaitingRepository waitingRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private EntityManager em;

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
        em.flush();
        em.clear();

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
        em.flush();
        em.clear();

        // when
        Long findResult = waitingRepository.findMaxWaitingOrderByStoreId(storeA.getStoreId());

        // then
        assertThat(findResult).isEqualTo(4L);
    }

    @Test
    @DisplayName("상점 id의 웨이팅(웨이팅 상태)이 존재하지 않을때 웨이팅 번호 0으로 가져오기")
    void findMaxWaitingOrderByStoreIdAndNotExistence() {
        // given
        Store storeA = storeRepository.save(Store.builder()
                .build());

        // when
        Long findResult = waitingRepository.findMaxWaitingOrderByStoreId(storeA.getStoreId());

        // then
        assertThat(findResult).isEqualTo(0L);
    }

    @Test
    @DisplayName("고객 id로 웨이팅 정보 전부 가져오기")
    void findAllByCustomerId() {
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
                    .customerInfo(new CustomerInfo(i-5, "010-1111-1111", ((long) (Math.random() * 3 + 1))))
                    .waitingStatus(WaitingStatus.VISITED)
                    .visitedTime(null)
                    .build());
        }
        em.flush();
        em.clear();


        // when
        List<Waiting> findResult = waitingRepository.findAllByCustomerId(2L);

        // then
        assertThat(findResult.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("상점 id로 웨이팅 상태인 웨이팅 개수 가져오기")
    void countWaitingByStoreIdAndWaitingStatus() {
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
        for (long i = 5; i < 15; i++) {
            waitingRepository.save(Waiting.builder()
                    .waitingNumber(i)
                    .waitingOrder(i)
                    .storeId(storeA.getStoreId())
                    .customerInfo(new CustomerInfo(i-5, "010-1111-1111", ((long) (Math.random() * 3 + 1))))
                    .waitingStatus(WaitingStatus.VISITED)
                    .visitedTime(null)
                    .build());
        }
        em.flush();
        em.clear();
        // when
        Integer findResult = waitingRepository.countWaitingByStoreIdAndWaitingStatus(storeA.getStoreId());

        // then
        assertThat(findResult).isEqualTo(5);
    }

    @Test
    @DisplayName("상점 id로 존재하지 않는 웨이팅 상태인 웨이팅 개수 가져오기")
    void countWaitingByStoreIdAndWaitingStatusAndNotExistence() {
        // given
        Store storeA = storeRepository.save(Store.builder()
                .build());

        for (long i = 5; i < 10; i++) {
            waitingRepository.save(Waiting.builder()
                    .waitingNumber(i)
                    .waitingOrder(i)
                    .storeId(storeA.getStoreId())
                    .customerInfo(new CustomerInfo(i-5, "010-1111-1111", ((long) (Math.random() * 3 + 1))))
                    .waitingStatus(WaitingStatus.VISITED)
                    .visitedTime(null)
                    .build());
        }
        em.flush();
        em.clear();
        // when
        Integer findResult = waitingRepository.countWaitingByStoreIdAndWaitingStatus(storeA.getStoreId());

        // then
        assertThat(findResult).isEqualTo(0);
    }

    @Test
    @DisplayName("웨이팅 id로 고객이 웨이팅 취소")
    void cancelWaitingByCustomer() {
        // given
        Store storeA = storeRepository.save(Store.builder()
                .build());

        Waiting waiting = waitingRepository.save(Waiting.builder()
                .waitingNumber(1L)
                .waitingOrder(1L)
                .storeId(storeA.getStoreId())
                .customerInfo(new CustomerInfo(1L, "010-1111-1111", ((long) (Math.random() * 3 + 1))))
                .waitingStatus(WaitingStatus.VISITED)
                .visitedTime(null)
                .build());
        em.flush();
        em.clear();

        // when
        int result = waitingRepository.cancelWaitingByCustomer(waiting.getId());

        // then
        assertThat(result).isEqualTo(1);
        assertThat(waitingRepository.findById(waiting.getId()).get().getWaitingStatus()).isEqualTo(WaitingStatus.CUSTOMER_CANCELED);
    }
}
