package com.matdang.seatdang.waiting.service.facade;

import com.matdang.seatdang.waiting.service.WaitingSettingService;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedissonLockWaitingSettingFacade {
    private final RedissonClient redissonClient;
    private final WaitingSettingService waitingSettingService;

    public int changeWaitingStatus(int status, Long storeId) {
        RLock lock = redissonClient.getLock("waitingLock:" + storeId);
        lock.lock(3, TimeUnit.SECONDS);

        try {
            return waitingSettingService.changeWaitingStatus(status, storeId);
        } finally {
            lock.unlock();
        }
    }
}
