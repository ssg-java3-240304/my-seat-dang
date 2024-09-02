package com.matdang.seatdang.waiting.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.matdang.seatdang.common.annotation.DoNotUse;
import com.matdang.seatdang.store.repository.StoreRepository;
import com.matdang.seatdang.store.repository.query.dto.AvailableWaitingTime;
import com.matdang.seatdang.store.repository.query.dto.StoreQueryRepository;
import com.matdang.seatdang.store.vo.WaitingStatus;
import com.matdang.seatdang.waiting.redis.Waiting;
import com.matdang.seatdang.waiting.service.facade.RedissonLockWaitingSettingFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WaitingSettingService {
    private final StoreQueryRepository storeQueryRepository;
    private final StoreRepository storeRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public AvailableWaitingTime findAvailableWaitingTime(Long storeId) {
        AvailableWaitingTime findResult = storeQueryRepository.findAvailableWaitingTime(storeId);
        if (findResult.getWaitingOpenTime() == null) {
            return new AvailableWaitingTime(LocalTime.of(0, 0), LocalTime.of(0, 0));
        }

        return findResult;
    }

    public LocalTime findEstimatedWaitingTime(Long storeId) {
        LocalTime findResult = storeQueryRepository.findEstimatedWaitingTime(storeId);
        if (findResult == null) {
            return LocalTime.of(0, 0);
        }

        return findResult;
    }

    public Waiting convertStringToWaiting(Object jsonString) {
        try {
            // JSON 문자열을 Waiting 객체로 역직렬화
            return objectMapper.readValue((String) jsonString, Waiting.class);
        } catch (Exception e) {
            e.printStackTrace(); // 예외 처리: 로그 기록
            return null;
        }
    }

    public void saveWaitingsToRedis(Map<Long, Waiting> waitings, Long storeId) {
        String storeKey = "store:" + storeId;

        Map<String, String> hashEntries = new HashMap<>();
        for (Map.Entry<Long, Waiting> entry : waitings.entrySet()) {
            Long waitingNumber = entry.getKey();
            Waiting waiting = entry.getValue();
            String waitingJson = null;
            try {
                waitingJson = objectMapper.writeValueAsString(waiting);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            hashEntries.put(waitingNumber.toString(), waitingJson);
        }

        // Redis Hash에 저장
        redisTemplate.opsForHash().putAll(storeKey, hashEntries);
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
            int result = storeRepository.updateWaitingStatus(WaitingStatus.UNAVAILABLE, storeId);
            Map<Long, Waiting> updatedWaitings = redisTemplate.opsForHash().values(key).stream()
                    .map(this::convertStringToWaiting)
                    .filter(waiting -> waiting.getWaitingStatus()==com.matdang.seatdang.waiting.entity.WaitingStatus.WAITING)
                    .peek(waiting -> {
                        waiting.setWaitingStatus(com.matdang.seatdang.waiting.entity.WaitingStatus.SHOP_CANCELED);
                        waiting.setCanceledTime(LocalDateTime.now());
                        decreaseWaitingOrder(storeId);
                    })
                    .collect(Collectors.toMap(Waiting::getWaitingNumber, waiting -> waiting));

            saveWaitingsToRedis(updatedWaitings, storeId);

        }

    }

    private Long decreaseWaitingOrder(Long storeId) {
        String waitingOrderKey = "waitingOrder:" + storeId;
        // Redis에서 waitingOrder 값을 1씩 감소시키고 반환
        return redisTemplate.opsForValue().increment(waitingOrderKey, -1);
    }

    /**
     * 상점 생성시, StoreSetting이 생성되면, null값이 들어오지 않아 삭제해도 됨 test code 생략
     */
    public WaitingStatus findWaitingStatus(Long storeId) {
        WaitingStatus findResult = storeQueryRepository.findWaitingStatus(storeId);
        if (findResult == null) {
            return WaitingStatus.UNAVAILABLE;
        }
        return findResult;
    }

    public Integer findWaitingPeopleCount(Long storeId) {
        Integer findResult = storeQueryRepository.findWaitingPeopleCount(storeId);
        if (findResult == null) {
            return 0;
        }

        return findResult;
    }
}
