package com.matdang.seatdang.waiting.service;

import com.matdang.seatdang.auth.service.AuthService;
import com.matdang.seatdang.member.entity.Customer;
import com.matdang.seatdang.store.entity.Store;
import com.matdang.seatdang.store.repository.StoreRepository;
import com.matdang.seatdang.waiting.dto.UpdateRequest;
import com.matdang.seatdang.waiting.dto.WaitingId;
import com.matdang.seatdang.waiting.entity.CustomerInfo;
import com.matdang.seatdang.waiting.redis.Waiting;
import com.matdang.seatdang.waiting.entity.WaitingStatus;
import com.matdang.seatdang.waiting.repository.WaitingStorageRepository;
import jakarta.persistence.EntityManager;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@Transactional
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class WaitingCustomerServiceTest {
    @Autowired
    private WaitingCustomerService waitingCustomerService;
    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private WaitingStorageRepository waitingStorageRepository;
    @Autowired
    private WaitingService waitingService;
    @Autowired
    private EntityManager em;
    @MockBean
    private AuthService authService;


//    @Test
//    @DisplayName("웨이팅 등록")
//    void createWaiting() {
//        // given
//        Store storeA = storeRepository.save(Store.builder()
//                .build());
//        for (long i = 0; i < 5; i++) {
//            waitingRepository.save(Waiting.builder()
//                    .waitingNumber(i)
//                    .waitingOrder(i)
//                    .storeId(storeA.getStoreId())
//                    .customerInfo(new CustomerInfo(i, "010-1111-1111", ((int) (Math.random() * 3 + 1))))
//                    .waitingStatus(WaitingStatus.WAITING)
//                    .visitedTime(null)
//                    .build());
//        }
//        for (long i = 5; i < 10; i++) {
//            waitingRepository.save(Waiting.builder()
//                    .waitingNumber(i)
//                    .waitingOrder(i)
//                    .storeId(storeA.getStoreId())
//                    .customerInfo(new CustomerInfo(i, "010-1111-1111", ((int) (Math.random() * 3 + 1))))
//                    .waitingStatus(WaitingStatus.VISITED)
//                    .visitedTime(null)
//                    .build());
//        }
//
//        Store storeB = storeRepository.save(Store.builder()
//                .build());
//        for (long i = 0; i < 5; i++) {
//            waitingRepository.save(Waiting.builder()
//                    .waitingNumber(i)
//                    .waitingOrder(i)
//                    .storeId(storeB.getStoreId())
//                    .customerInfo(new CustomerInfo(i, "010-1111-1111", ((int) (Math.random() * 3 + 1))))
//                    .waitingStatus(WaitingStatus.WAITING)
//                    .visitedTime(null)
//                    .build());
//        }
//        em.flush();
//        em.clear();
//
//        Customer mockCustomer = Customer.builder()
//                .memberId(50L)
//                .memberPhone("010-1234-1234")
//                .build();
//        when(authService.getAuthenticatedMember()).thenReturn(mockCustomer);
//        // when
//        waitingCustomerService.createWaiting(storeA.getStoreId(), 2);
//        em.flush();
//        em.clear();
//        List<Waiting> findResult = waitingRepository.findAllByCustomerId(50L);
//
//        // then
//
//        assertThat(findResult.size()).isEqualTo(1);
//        assertThat(findResult.get(0).getWaitingOrder()).isEqualTo(5L);
//        assertThat(findResult.get(0).getWaitingNumber()).isEqualTo(10L);
//        assertThat(findResult.get(0).getCustomerInfo().getCustomerPhone()).isEqualTo("010-1234-1234");
//
//    }

//    @ParameterizedTest
//    @CsvSource(value = {"0,20", "1,20", "2,60"})
//    @DisplayName("웨이팅 상태별 조회")
//    void showTodayWaiting(int status, int size) {
//        Store storeA = storeRepository.save(Store.builder()
//                .storeName("마싯당")
//                .build());
//        // given
//        {
//            long i = 0;
//            for (WaitingStatus value : WaitingStatus.values()) {
//                for (int j = 0; j < 10; j++, i++) {
//                    waitingRepository.save(Waiting.builder()
//                            .waitingNumber(i)
//                            .waitingOrder(i)
//                            .storeId(storeA.getStoreId())
//                            .customerInfo(new CustomerInfo(1L, "010-1111-1111", ((int) (Math.random() * 3 + 1))))
//                            .waitingStatus(value)
//                            .visitedTime(null)
//                            .build());
//                    waitingStorageRepository.save(WaitingStorage.builder()
//                            .waitingNumber(i)
//                            .waitingOrder(i)
//                            .storeId(storeA.getStoreId())
//                            .customerInfo(new CustomerInfo(1L, "010-1111-1111", ((int) (Math.random() * 3 + 1))))
//                            .waitingStatus(value)
//                            .visitedTime(null)
//                            .build());
//
//                }
//            }
//        }
//
//        for (long i = 0; i < 10; i++) {
//            waitingRepository.save(Waiting.builder()
//                    .waitingNumber(i)
//                    .waitingOrder(i)
//                    .storeId(2L)
//                    .customerInfo(new CustomerInfo(2L, "010-1111-1111", ((int) (Math.random() * 3 + 1))))
//                    .waitingStatus(WaitingStatus.WAITING)
//                    .visitedTime(null)
//                    .build());
//        }
//        em.flush();
//        em.clear();
//
//        Customer mockCustomer = Customer.builder()
//                .memberId(1L)
//                .memberPhone("010-1234-1234")
//                .build();
//        when(authService.getAuthenticatedMember()).thenReturn(mockCustomer);
//
//        // when
//        Page<WaitingInfoProjection> findResult = waitingCustomerService.showTodayWaiting(status, 0);
//
//        // then
//        assertThat(findResult.getTotalElements()).isEqualTo(size);
//    }

//    @Disabled
//    @Test
//    @DisplayName("웨이팅 id로 웨이팅 취소")
//    void cancelWaitingByCustomer() {
//        // given
//        Waiting waiting = waitingRepository.save(Waiting.builder()
//                .waitingNumber(0L)
//                .waitingOrder(0L)
//                .storeId(1L)
//                .customerInfo(new CustomerInfo(1L, "010-1111-1111", ((int) (Math.random() * 3 + 1))))
//                .waitingStatus(WaitingStatus.WAITING)
//                .visitedTime(null)
//                .build());
//
//        waitingRepository.save(Waiting.builder()
//                .waitingNumber(1L)
//                .waitingOrder(1L)
//                .storeId(1L)
//                .customerInfo(new CustomerInfo(1L, "010-1111-1111", ((int) (Math.random() * 3 + 1))))
//                .waitingStatus(WaitingStatus.WAITING)
//                .visitedTime(null)
//                .build());
//        waitingRepository.save(Waiting.builder()
//                .waitingNumber(2L)
//                .waitingOrder(2L)
//                .storeId(1L)
//                .customerInfo(new CustomerInfo(1L, "010-1111-1111", ((int) (Math.random() * 3 + 1))))
//                .waitingStatus(WaitingStatus.WAITING)
//                .visitedTime(null)
//                .build());
//        em.flush();
//        em.clear();
//
//        // when
////        int result = waitingCustomerService.cancelWaitingByCustomer(waiting.getId());
////
////        // then
////        assertThat(waitingRepository.findById(waiting.getId()).get().getWaitingStatus()).isEqualTo(
////                WaitingStatus.CUSTOMER_CANCELED);
////        assertThat(result).isEqualTo(3);
//    }

//    @Test
//    @DisplayName(" 웨이팅 100개 등록")
//    void createWaitingByNotConcurrency() {
//        // given
//        Customer mockCustomer = Customer.builder()
//                .memberId(1L)
//                .memberPhone("010-1234-1234")
//                .build();
//        when(authService.getAuthenticatedMember()).thenReturn(mockCustomer);
//        // when
//        for (int i = 0; i < 100; i++) {
//            waitingCustomerService.createWaiting(1L, 2);
//        }
//
//        // then
//        Long findResult = waitingRepository.findMaxWaitingOrderByStoreId(1L);
//        assertThat(findResult).isEqualTo(100);
//    }
//
//    //    @Disabled
//    @Test
////    @Rollback(value = false)
//    @DisplayName("동시에 웨이팅 50개 등록 동시성 테스트")
//    void createWaitingByConcurrency() throws InterruptedException {
//        // given
//        int threadCount = 50;
//        ExecutorService executorService = Executors.newFixedThreadPool(8);
//        CountDownLatch latch = new CountDownLatch(threadCount);
//
//        Customer mockCustomer = Customer.builder()
//                .memberId(1L)
//                .memberPhone("010-1234-1234")
//                .build();
//        when(authService.getAuthenticatedMember()).thenReturn(mockCustomer);
//        // when
//        for (int i = 0; i < threadCount; i++) {
//            executorService.execute(() -> {
//                try {
//                    waitingCustomerService.createWaiting(1L, 2);
//                } finally {
//                    latch.countDown();
//                }
//            });
//        }
//        latch.await();
//        executorService.shutdown();
//
//        // then
//        Long findResult = waitingRepository.findMaxWaitingOrderByStoreId(1L);
//
//        assertThat(findResult).isEqualTo(50);
//    }

    @ParameterizedTest
    @CsvSource(value = {"1,true", "2,false"})
    @DisplayName("해당 상점에 웨이팅이 존재 유무 확인")
    void isWaitingExists(Long storeId, boolean result) {
        // given
        Customer mockCustomer = Customer.builder()
                .memberId(1L)
                .memberPhone("010-1234-1234")
                .build();
        when(authService.getAuthenticatedMember()).thenReturn(mockCustomer);

        waitingCustomerService.createWaiting(1L, 1);
        // when
        boolean waitingExists = waitingCustomerService.isWaitingExists(storeId);

        // then
        assertThat(waitingExists).isEqualTo(result);
    }

    @ParameterizedTest
    @CsvSource(value = {"awaiting,false", "visited,true", "canceled,true"})
    @DisplayName("웨이팅일 때, 웨이팅 URL과 비교")
    void isIncorrectWaitingStatusByAwaiting(String status, boolean result) {
        // given
        Customer mockCustomer = Customer.builder()
                .memberId(1L)
                .memberPhone("010-1234-1234")
                .build();
        when(authService.getAuthenticatedMember()).thenReturn(mockCustomer);

        waitingCustomerService.createWaiting(1L, 1);

        // when
        boolean isIncorrectWaiting = waitingCustomerService.isIncorrectWaitingStatus(1L, 1L, status,"today");

        // then
        assertThat(isIncorrectWaiting).isEqualTo(result);
    }

    @ParameterizedTest
    @CsvSource(value = {"1,visited,false", "2,visited,true", "1,canceled,true", "2,canceled,false"})
    @DisplayName("웨이팅 입장,취소 상태일 때 URL과 비교")
    void isIncorrectWaitingStatusByVisitedAndCanceled(int waitingStatus, String checkStatus, boolean result) {

        // given
        Customer mockCustomer = Customer.builder()
                .memberId(1L)
                .memberPhone("010-1234-1234")
                .build();
        when(authService.getAuthenticatedMember()).thenReturn(mockCustomer);

        WaitingId waiting = waitingCustomerService.createWaiting(1L, 1);

        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.setChangeStatus(waitingStatus);
        updateRequest.setStoreId(1L);
        updateRequest.setWaitingNumber(waiting.getWaitingNumber());
        updateRequest.setWaitingOrder(1L);

        waitingService.updateStatus(updateRequest);
        // when
        boolean isIncorrectWaiting = waitingCustomerService.isIncorrectWaitingStatus(1L, waiting.getWaitingNumber(),
                checkStatus, "today");

        // then
        assertThat(isIncorrectWaiting).isEqualTo(result);
    }


    @Test
    @DisplayName("웨이팅 일때, false 반환")
    void isNotAwaitingByWaiting() {
        // given
        Customer mockCustomer = Customer.builder()
                .memberId(1L)
                .memberPhone("010-1234-1234")
                .build();
        when(authService.getAuthenticatedMember()).thenReturn(mockCustomer);
        WaitingId waiting = waitingCustomerService.createWaiting(1L, 1);

        // when
        boolean isIncorrectWaiting = waitingCustomerService.isNotAwaiting(1L, waiting.getWaitingNumber());

        // then
        assertThat(isIncorrectWaiting).isFalse();
    }

    @Test
    @DisplayName("웨이팅이 취소 상태 일때, true 반환")
    void isNotAwaitingByCanceled() {
        // given
        Customer mockCustomer = Customer.builder()
                .memberId(1L)
                .memberPhone("010-1234-1234")
                .build();
        when(authService.getAuthenticatedMember()).thenReturn(mockCustomer);
        WaitingId waiting = waitingCustomerService.createWaiting(1L, 1);


        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.setChangeStatus(2);
        updateRequest.setStoreId(1L);
        updateRequest.setWaitingNumber(waiting.getWaitingNumber());
        updateRequest.setWaitingOrder(1L);

        waitingService.updateStatus(updateRequest);

        // when
        boolean isIncorrectWaiting = waitingCustomerService.isNotAwaiting(1L, waiting.getWaitingNumber());

        // then
        assertThat(isIncorrectWaiting).isTrue();
    }

//    @Disabled
//    @Test
//    @DisplayName("웨이팅 10개 취소")
//    void cancelWaitingByNotConcurrency() {
//        // given
//        Customer mockCustomer = Customer.builder()
//                .memberId(1L)
//                .memberPhone("010-1234-1234")
//                .build();
//        when(authService.getAuthenticatedMember()).thenReturn(mockCustomer);
//        List<Long> waitingIds = new ArrayList<>();
//        for (int i = 0; i < 50; i++) {
//            Long id = waitingCustomerService.createWaiting(1L, 2);
//            waitingIds.add(id);
//        }

    // when
//        for (int i = 0; i < 10; i++) {
//            waitingCustomerService.cancelWaitingByCustomer(waitingIds.get(i));
//        }
//
//        // then
//        Long findResult = waitingRepository.findMaxWaitingOrderByStoreId(1L);
//
//        assertThat(findResult).isEqualTo(40);

}

