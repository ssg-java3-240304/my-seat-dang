package com.matdang.seatdang.waiting.service.facade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.matdang.seatdang.auth.service.AuthService;
import com.matdang.seatdang.member.entity.Customer;
import com.matdang.seatdang.waiting.repository.WaitingRepository;
import com.matdang.seatdang.waiting.service.WaitingCustomerService;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class RedissonLockWaitingFacadeTest {
    @Autowired
    private RedissonLockWaitingFacade redissonLockWaitingFacade;

    @Autowired
    private WaitingRepository waitingRepository;
    @MockBean
    private AuthService authService;

    @Test
    @DisplayName("동시에 웨이팅 100개 등록 RedissonLock 동시성 테스트")
    void createWaitingByConcurrency() throws InterruptedException {
        // given
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        Customer mockCustomer = Customer.builder()
                .memberId(1L)
                .memberPhone("010-1234-1234")
                .build();
        when(authService.getAuthenticatedMember()).thenReturn(mockCustomer);
        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    redissonLockWaitingFacade.createWaiting(1L, 2);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        executorService.shutdown();

        // then
        Long findResult = waitingRepository.findMaxWaitingOrderByStoreId(1L);

        assertThat(findResult).isEqualTo(threadCount);
        // 2174ms, 2040ms, 2030ms
    }


    @Test
    @DisplayName("동시에 웨이팅 85개 취소 동시성 테스트")
    void cancelWaitingByConcurrency() throws InterruptedException {
        // given
        Customer mockCustomer = Customer.builder()
                .memberId(1L)
                .memberPhone("010-1234-1234")
                .build();
        when(authService.getAuthenticatedMember()).thenReturn(mockCustomer);
        List<Long> waitingIds = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Long id = redissonLockWaitingFacade.createWaiting(1L, 2);
            waitingIds.add(id);
        }

        int threadCount = 85;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            long waitingId = waitingIds.get(i);
            executorService.execute(() -> {
                try {
                    redissonLockWaitingFacade.cancelWaitingByCustomer(waitingId);
                } catch (Exception e) {
                    e.printStackTrace(); // 예외를 출력합니다
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        executorService.shutdown();

        // then
        Long findResult = waitingRepository.findMaxWaitingOrderByStoreId(1L);
        assertThat(findResult).isEqualTo(15);
    }
    @Test
    @DisplayName("웨이팅 100개 등록된 상태에서 등록 50번 + 취소50번 동시성 테스트")
    void createAndCancelWaitingByConcurrency() throws InterruptedException {
        // given
        Customer mockCustomer = Customer.builder()
                .memberId(1L)
                .memberPhone("010-1234-1234")
                .build();
        when(authService.getAuthenticatedMember()).thenReturn(mockCustomer);
        List<Long> waitingIds = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Long id = redissonLockWaitingFacade.createWaiting(1L, 2);
            waitingIds.add(id);
        }

        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < 50; i++) {
            // 등록 50번
            executorService.execute(() -> {
                try {
                    redissonLockWaitingFacade.createWaiting(1L, 2);
                } catch (Exception e) {
                    e.printStackTrace(); // 예외를 출력합니다
                } finally {
                    latch.countDown();
                }
            });

            long waitingId = waitingIds.get(i);
            executorService.execute(() -> {
                try {
                    redissonLockWaitingFacade.cancelWaitingByCustomer(waitingId);
                } catch (Exception e) {
                    e.printStackTrace(); // 예외를 출력합니다
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        executorService.shutdown();

        // then
        Long findResult = waitingRepository.findMaxWaitingOrderByStoreId(1L);
        assertThat(findResult).isEqualTo(100);
    }
}