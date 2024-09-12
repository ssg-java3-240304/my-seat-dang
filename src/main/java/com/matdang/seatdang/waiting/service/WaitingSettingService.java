package com.matdang.seatdang.waiting.service;

import com.matdang.seatdang.common.annotation.DoNotUse;
import com.matdang.seatdang.store.repository.StoreRepository;
import com.matdang.seatdang.store.repository.query.dto.AvailableWaitingTime;
import com.matdang.seatdang.store.repository.query.dto.StoreQueryRepository;
import com.matdang.seatdang.store.vo.WaitingStatus;
import com.matdang.seatdang.waiting.redis.Waiting;
import com.matdang.seatdang.waiting.service.facade.RedissonLockWaitingSettingFacade;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WaitingSettingService {
    private final StoreQueryRepository storeQueryRepository;
    private final StoreRepository storeRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisTemplate<String, Waiting> waitingRedisTemplate;

    private HashOperations<String, Long, Waiting> waitingHashOps;
    private ValueOperations<String, Object> valueOps;

    @PostConstruct
    public void init() {
        this.valueOps = redisTemplate.opsForValue();
        this.waitingHashOps = waitingRedisTemplate.opsForHash();
    }


    @Transactional(readOnly = true)
    public AvailableWaitingTime findAvailableWaitingTime(Long storeId) {
        AvailableWaitingTime findResult = storeQueryRepository.findAvailableWaitingTime(storeId);
        if (findResult.getWaitingOpenTime() == null) {
            return new AvailableWaitingTime(LocalTime.of(0, 0), LocalTime.of(0, 0));
        }

        return findResult;
    }

    @Transactional(readOnly = true)
    public LocalTime findEstimatedWaitingTime(Long storeId) {
        LocalTime findResult = storeQueryRepository.findEstimatedWaitingTime(storeId);
        if (findResult == null) {
            return LocalTime.of(0, 0);
        }

        return findResult;
    }

    /**
     * {@link RedissonLockWaitingSettingFacade#changeWaitingStatus(int, Long)}을 사용하세요.
     */
    @DoNotUse(message = "이 메서드를 직접 사용하지 마세요.")
    @Transactional
    public void changeWaitingStatus(int status, Long storeId) {
        String key = "store:" + storeId;
        if (status == 1) {
            storeRepository.updateWaitingStatus(WaitingStatus.OPEN, storeId);
            return;
        }
        if (status == 2) {
            storeRepository.updateWaitingStatus(WaitingStatus.CLOSE, storeId);
            return;
        }
        if (status == 3) {
            storeRepository.updateWaitingStatus(WaitingStatus.UNAVAILABLE, storeId);
            Map<Long, Waiting> updatedWaitings = waitingHashOps.entries(key).entrySet().stream()
                    .filter(entry -> entry.getValue().getWaitingStatus()
                            == com.matdang.seatdang.waiting.entity.WaitingStatus.WAITING)
                    .map(entry -> {
                        entry.getValue()
                                .setWaitingStatus(com.matdang.seatdang.waiting.entity.WaitingStatus.SHOP_CANCELED);
                        entry.getValue().setCanceledTime(LocalDateTime.now());
                        decreaseWaitingOrder(storeId);
                        return entry;
                    })
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            waitingHashOps.putAll(key, updatedWaitings);
        }
    }

    private Long decreaseWaitingOrder(Long storeId) {
        String waitingOrderKey = "waitingOrder:" + storeId;
        // Redis에서 waitingOrder 값을 1씩 감소시키고 반환
        return valueOps.increment(waitingOrderKey, -1);
    }

    /**
     * 상점 생성시, StoreSetting이 생성되면, null값이 들어오지 않아 삭제해도 됨 test code 생략
     */
    @Transactional(readOnly = true)
    public WaitingStatus findWaitingStatus(Long storeId) {
        WaitingStatus findResult = storeQueryRepository.findWaitingStatus(storeId);
        if (findResult == null) {
            return WaitingStatus.UNAVAILABLE;
        }
        return findResult;
    }

    @Transactional(readOnly = true)
    public Integer findWaitingPeopleCount(Long storeId) {
        Integer findResult = storeQueryRepository.findWaitingPeopleCount(storeId);
        if (findResult == null) {
            return 0;
        }

        return findResult;
    }
}
