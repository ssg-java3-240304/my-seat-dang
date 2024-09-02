//package com.matdang.seatdang.waiting.service.facade;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.when;
//
//import com.matdang.seatdang.auth.service.AuthService;
//import com.matdang.seatdang.member.entity.Customer;
//import com.matdang.seatdang.waiting.repository.WaitingRepository;
//import jakarta.persistence.EntityManager;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.transaction.annotation.Transactional;
//
//@SpringBootTest
//@Transactional
//
//class NamedLockWaitingFacadeTest {
//    @Autowired
//    private NamedLockWaitingFacade namedLockWaitingFacade;
//
//    @Autowired
//    private WaitingRepository waitingRepository;
//    @Autowired
//    private EntityManager em;
//    @MockBean
//    private AuthService authService;
//
//    @Test
////    @Rollback(value = false)
//    @DisplayName("동시에 웨이팅 100개 등록 NamedLock 동시성 테스트")
//    void createWaitingByConcurrency() throws InterruptedException {
//        // given
//        int threadCount = 100;
//        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
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
//                    namedLockWaitingFacade.createWaiting(1L, 2);
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
//        assertThat(findResult).isEqualTo(threadCount);
//    }
//    // 2153, 2220, 2204 ms
//}