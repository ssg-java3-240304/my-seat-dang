package com.matdang.seatdang.waiting.service.facade;

import com.matdang.seatdang.waiting.service.WaitingCustomerService;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RedissonLockWaitingFacade {
    private final RedissonClient redissonClient; // RedissonClient 추가
    private final WaitingCustomerService waitingCustomerService;

    public Long createWaiting(Long storeId, Integer peopleCount) {
        RLock lock = redissonClient.getLock("waitingCreateLock:" + storeId); // 락 키 설정
        lock.lock(3, TimeUnit.SECONDS); // 락 획득

        try {
            return waitingCustomerService.createWaiting(storeId, peopleCount);
        } finally {
            lock.unlock(); // 락 해제
        }
    }

    public int cancelWaitingByCustomer(Long waitingId) {
        RLock lock = redissonClient.getLock("waitingCancelLock:" + waitingId); // 락 키 설정
        lock.lock(3, TimeUnit.SECONDS); // 락 획득

        try {
            return waitingCustomerService.cancelWaitingByCustomer(waitingId);
        } finally {
            lock.unlock();
        }
    }
}
