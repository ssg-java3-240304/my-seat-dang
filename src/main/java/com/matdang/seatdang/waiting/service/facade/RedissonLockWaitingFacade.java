package com.matdang.seatdang.waiting.service.facade;

import com.matdang.seatdang.waiting.dto.UpdateRequest;
import com.matdang.seatdang.waiting.service.WaitingService;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedissonLockWaitingFacade {
    private final RedissonClient redissonClient; // RedissonClient 추가
    private final WaitingService waitingService;


    public void updateStatus(UpdateRequest updateRequest) {
        RLock lock = redissonClient.getLock("waitingLock:" + updateRequest.getStoreId());
        lock.lock(3, TimeUnit.SECONDS);

        try {
            waitingService.updateStatus(updateRequest);
        } finally {
            lock.unlock(); // 락 해제
        }
    }
}
