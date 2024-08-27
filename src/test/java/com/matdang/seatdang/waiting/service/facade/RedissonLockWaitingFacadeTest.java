package com.matdang.seatdang.waiting.service.facade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.matdang.seatdang.auth.service.AuthService;
import com.matdang.seatdang.member.entity.Customer;
import com.matdang.seatdang.waiting.dto.UpdateRequest;
import com.matdang.seatdang.waiting.dto.WaitingId;
import com.matdang.seatdang.waiting.entity.Waiting;
import com.matdang.seatdang.waiting.entity.WaitingStatus;
import com.matdang.seatdang.waiting.repository.WaitingRepository;
import com.matdang.seatdang.waiting.service.WaitingCustomerService;
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
class RedissonLockWaitingFacadeTest {
    @Autowired
    private RedissonLockWaitingCustomerFacade redissonLockWaitingCustomerFacade;
    @Autowired
    private RedissonLockWaitingFacade redissonLockWaitingFacade;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private WaitingService waitingService;
    @MockBean
    private AuthService authService;

    @Test
    @DisplayName("기존 웨이팅 150개 (등록50 + 취소50 + 점주 입장처리 50) 동시성 테스트")
    void updateStatusByVisited() throws InterruptedException {
        // given
        Customer mockCustomer = Customer.builder()
                .memberId(1L)
                .memberPhone("010-1234-1234")
                .build();
        when(authService.getAuthenticatedMember()).thenReturn(mockCustomer);
        List<Long> waitingIds = new ArrayList<>();
        for (int i = 0; i < 150; i++) {
            Long id = redissonLockWaitingCustomerFacade.createWaiting(1L, 2).getWaitingNumber();
            waitingIds.add(id);
        }

        List<UpdateRequest> updateRequests = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            UpdateRequest updateRequest = new UpdateRequest();
            updateRequest.setChangeStatus(1);
            updateRequest.setStoreId(1L);
            updateRequest.setWaitingNumber(waitingIds.get(i + 50));
            updateRequest.setWaitingOrder(waitingIds.get(i + 50));

            updateRequests.add(updateRequest);
        }

        int threadCount = 150;
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

            long waitingId = waitingIds.get(i);
            executorService.execute(() -> {
                try {
                    redissonLockWaitingCustomerFacade.cancelWaitingByCustomer(waitingId, 1L);
                } catch (Exception e) {
                    e.printStackTrace(); // 예외를 출력합니다
                } finally {
                    latch.countDown();
                }
            });

            UpdateRequest updateRequest = updateRequests.get(i);
            executorService.execute(() -> {
                try {
                    redissonLockWaitingFacade.updateStatus(updateRequest);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        executorService.shutdown();

        // then
        String max = (String) redisTemplate.opsForValue().get("waitingOrder:1");
        assertThat(Integer.parseInt(max)).isEqualTo(100);

        List<Waiting> waitingList = redisTemplate.opsForHash().values("store:1").stream()
                .map(waiting -> waitingService.convertStringToWaiting(waiting))
                .toList();
        int waitingCount = 0;
        int visitedCount = 0;
        for (Waiting waiting : waitingList) {
            if (waiting.getWaitingStatus() == WaitingStatus.WAITING) {
                waitingCount++;
            }
            if (waiting.getWaitingStatus() == WaitingStatus.VISITED) {
                visitedCount++;
            }
        }
        assertThat(waitingCount).isEqualTo(100);
        assertThat(visitedCount).isEqualTo(50);
    }

//    @Test
//    @DisplayName("기존 웨이팅 150개 (등록50 + 취소50 + 점주 취소처리 50) 동시성 테스트")
//    void updateStatusByCanceled() throws InterruptedException {
//        // given
//        Customer mockCustomer = Customer.builder()
//                .memberId(1L)
//                .memberPhone("010-1234-1234")
//                .build();
//        when(authService.getAuthenticatedMember()).thenReturn(mockCustomer);
//        List<Long> waitingIds = new ArrayList<>();
//        for (int i = 0; i < 150; i++) {
//            Long id = redissonLockWaitingCustomerFacade.createWaiting(1L, 2);
//            waitingIds.add(id);
//        }
//
//        List<UpdateRequest> updateRequests = new ArrayList<>();
//        for (int i = 0; i < 50; i++) {
//            UpdateRequest updateRequest = new UpdateRequest();
//            updateRequest.setChangeStatus(2);
//            updateRequest.setStoreId(1L);
//            updateRequest.setId(waitingIds.get(i + 50));
//            updateRequest.setWaitingOrder(waitingIds.get(i + 50));
//
//            updateRequests.add(updateRequest);
//        }
//
//        int threadCount = 150;
//        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
//        CountDownLatch latch = new CountDownLatch(threadCount);
//
//        // when
//        for (int i = 0; i < 50; i++) {
//            // 등록 50번
//            executorService.execute(() -> {
//                try {
//                    redissonLockWaitingCustomerFacade.createWaiting(1L, 2);
//                } catch (Exception e) {
//                    e.printStackTrace(); // 예외를 출력합니다
//                } finally {
//                    latch.countDown();
//                }
//            });
//
//            long waitingId = waitingIds.get(i);
//            executorService.execute(() -> {
//                try {
//                    redissonLockWaitingCustomerFacade.cancelWaitingByCustomer(waitingId);
//                } catch (Exception e) {
//                    e.printStackTrace(); // 예외를 출력합니다
//                } finally {
//                    latch.countDown();
//                }
//            });
//
//            UpdateRequest updateRequest = updateRequests.get(i);
//            executorService.execute(() -> {
//                try {
//                    redissonLockWaitingFacade.updateStatus(updateRequest);
//                } catch (Exception e) {
//                    e.printStackTrace(); // 예외를 출력합니다
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
//        assertThat(findResult).isEqualTo(100);
//
//        assertThat(waitingRepository.countWaitingByStoreIdAndWaitingStatus(1L)).isEqualTo(100);
//        List<Waiting> waitings = waitingRepository.findAllByStoreId(1L);
//        int count =0;
//        for (Waiting waiting : waitings) {
//            if (waiting.getWaitingStatus() == WaitingStatus.SHOP_CANCELED) {
//                count++;
//            }
//        }
//        assertThat(count).isEqualTo(50);
//
//    }
}
