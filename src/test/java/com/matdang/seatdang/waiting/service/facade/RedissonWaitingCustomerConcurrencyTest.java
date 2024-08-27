package com.matdang.seatdang.waiting.service.facade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.matdang.seatdang.auth.service.AuthService;
import com.matdang.seatdang.member.entity.Customer;
import com.matdang.seatdang.waiting.entity.Waiting;
import com.matdang.seatdang.waiting.entity.WaitingStatus;
import com.matdang.seatdang.waiting.service.WaitingService;
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
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class RedissonWaitingCustomerConcurrencyTest {
    @Autowired
    private RedissonLockWaitingCustomerFacade redissonLockWaitingCustomerFacade;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private WaitingService waitingService;
    @MockBean
    private AuthService authService;

    @Test
    @DisplayName("동시에 웨이팅 100개 등록 동시성 테스트")
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
                    redissonLockWaitingCustomerFacade.createWaiting(1L, 2);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        executorService.shutdown();

        // then
        String max =(String) redisTemplate.opsForValue().get("waitingOrder:1");
        assertThat(Integer.parseInt(max)).isEqualTo(threadCount);
        // 기존 Redisson Lock 사용시
        // 2174ms, 2040ms, 2030ms


        // 싱글 스레드 Redis 사용 - Redisson Lock 사용 X
        // 675ms, 778ms, 766ms

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
            Long id = redissonLockWaitingCustomerFacade.createWaiting(1L, 2).getWaitingNumber();
            waitingIds.add(id);
        }

        int threadCount = 85;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            long waitingNumber = waitingIds.get(i);
            executorService.execute(() -> {
                try {
                    redissonLockWaitingCustomerFacade.cancelWaitingByCustomer(waitingNumber, 1L);
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
        List<Waiting> waitings = redisTemplate.opsForHash().values("store:1").stream()
                .map(waiting -> waitingService.convertStringToWaiting(waiting))
                .filter(waiting -> waiting.getWaitingStatus() == WaitingStatus.CUSTOMER_CANCELED)
                .toList();
        assertThat(waitings.size()).isEqualTo(85);

        String max =(String) redisTemplate.opsForValue().get("waitingOrder:1");
        assertThat(Integer.parseInt(max)).isEqualTo(15);


        // 싱글 스레드 Redis 사용 - Redisson Lock 사용 X
        // 1577 ms, 1387ms, 1319ms
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
            Long id = redissonLockWaitingCustomerFacade.createWaiting(1L, 2).getWaitingNumber();
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
                    redissonLockWaitingCustomerFacade.createWaiting(1L, 2);
                } catch (Exception e) {
                    e.printStackTrace(); // 예외를 출력합니다
                } finally {
                    latch.countDown();
                }
            });

            long waitingNumber = waitingIds.get(i);
            executorService.execute(() -> {
                try {
                    redissonLockWaitingCustomerFacade.cancelWaitingByCustomer(waitingNumber, 1L);
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
        List<Waiting> waitings = redisTemplate.opsForHash().values("store:1").stream()
                .map(waiting -> waitingService.convertStringToWaiting(waiting))
                .toList();
        int waitingCount = 0;
        int canceledCount = 0;
        for (Waiting waiting : waitings) {
            if (waiting.getWaitingStatus() == WaitingStatus.WAITING) {
                waitingCount++;
            }
            if (waiting.getWaitingStatus() == WaitingStatus.CUSTOMER_CANCELED) {
                canceledCount++;
            }
        }

        assertThat(waitingCount).isEqualTo(100);
        assertThat(canceledCount).isEqualTo(50);

        String max =(String) redisTemplate.opsForValue().get("waitingOrder:1");
        assertThat(Integer.parseInt(max)).isEqualTo(100);

        // 싱글 스레드 Redis 사용 - Redisson Lock 사용 X
        // 1409ms, 1360ms, 1336ms
    }
}
