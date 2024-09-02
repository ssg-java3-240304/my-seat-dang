package com.matdang.seatdang.waiting.service.facade;

import com.matdang.seatdang.waiting.dto.WaitingId;
import com.matdang.seatdang.waiting.service.WaitingCustomerService;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class RedissonLockWaitingCustomerFacade {
    private final RedissonClient redissonClient; // RedissonClient 추가
    private final WaitingCustomerService waitingCustomerService;

    public WaitingId createWaiting(Long storeId, Integer peopleCount) {
        RLock lock = redissonClient.getLock("waitingLock:" + storeId); // 락 키 설정
        lock.lock(3, TimeUnit.SECONDS); // 락 획득

        try {
            return waitingCustomerService.createWaiting(storeId, peopleCount);
        } finally {
            lock.unlock(); // 락 해제
        }
    }

    public void cancelWaitingByCustomer(Long waitingNumber, Long storeId) {

        RLock lock = redissonClient.getLock("waitingLock:" + storeId); // 락 키 설정
        lock.lock(3, TimeUnit.SECONDS); // 락 획득

        try {
           waitingCustomerService.cancelWaitingByCustomer(waitingNumber, storeId);
        } finally {
            lock.unlock();
        }
    }
}
