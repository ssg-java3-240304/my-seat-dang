package com.matdang.seatdang.waiting.service.facade;

import com.matdang.seatdang.waiting.entity.Waiting;
import com.matdang.seatdang.waiting.repository.WaitingRepository;
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
public class RedissonLockWaitingFacade {
    private final RedissonClient redissonClient; // RedissonClient 추가
    private final WaitingCustomerService waitingCustomerService;
    private final WaitingRepository waitingRepository;

    public Long createWaiting(Long storeId, Integer peopleCount) {
        RLock lock = redissonClient.getLock("waitingLock:" + storeId); // 락 키 설정
        lock.lock(3, TimeUnit.SECONDS); // 락 획득

        try {
            return waitingCustomerService.createWaiting(storeId, peopleCount);
        } finally {
            lock.unlock(); // 락 해제
        }
    }

    public int cancelWaitingByCustomer(Long waitingId) {
        Optional<Waiting> optionalWaiting = waitingRepository.findById(waitingId);
        Waiting waiting = optionalWaiting.orElseThrow(
                () -> new NoSuchElementException("No waiting found with id: " + waitingId));

        RLock lock = redissonClient.getLock("waitingLock:" + waiting.getStoreId()); // 락 키 설정
        lock.lock(3, TimeUnit.SECONDS); // 락 획득

        try {
            return waitingCustomerService.cancelWaitingByCustomer(waiting);
        } finally {
            lock.unlock();
        }
    }
}
