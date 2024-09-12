package com.matdang.seatdang.waiting.service;

import com.matdang.seatdang.common.annotation.DoNotUse;
import com.matdang.seatdang.waiting.dto.UpdateRequest;
import com.matdang.seatdang.waiting.redis.Waiting;
import com.matdang.seatdang.waiting.repository.query.dto.WaitingDto;
import com.matdang.seatdang.waiting.entity.WaitingStatus;
import com.matdang.seatdang.waiting.service.facade.RedissonLockWaitingFacade;
import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WaitingService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisTemplate<String, Waiting> waitingRedisTemplate;
    private HashOperations<String, Long, Waiting> waitingHashOps;
    private ValueOperations<String, Object> valueOps;

    @PostConstruct
    public void init() {
        this.valueOps = redisTemplate.opsForValue();
        this.waitingHashOps = waitingRedisTemplate.opsForHash();
    }

    public Page<WaitingDto> showWaiting(Long storeId, int status, int page) {
        String key = "store:" + storeId + ":waiting";
        ;
        PageRequest pageable = PageRequest.of(page, 10);

        List<WaitingDto> waitingDtos = waitingHashOps.values(key).stream()
                .map(WaitingDto::create)
                .toList();

        List<WaitingDto> filteredWaitings;
        if (status == 0) {
            filteredWaitings = waitingDtos.stream()
                    .filter(dto -> dto.getWaitingStatus() == WaitingStatus.WAITING)
                    .sorted(Comparator.comparing(WaitingDto::getWaitingOrder))
                    .toList();
        } else if (status == 1) {
            filteredWaitings = waitingDtos.stream()
                    .filter(dto -> dto.getWaitingStatus() == WaitingStatus.VISITED)
                    .sorted(Comparator.comparing(WaitingDto::getVisitedTime).reversed())
                    .toList();
        } else if (status == 2) {
            filteredWaitings = waitingDtos.stream()
                    .filter(dto -> dto.getWaitingStatus() == WaitingStatus.SHOP_CANCELED
                            || dto.getWaitingStatus() == WaitingStatus.NO_SHOW
                            || dto.getWaitingStatus() == WaitingStatus.CUSTOMER_CANCELED)
                    .sorted(Comparator.comparing(WaitingDto::getCanceledTime).reversed())
                    .toList();
        } else {
            filteredWaitings = List.of(); // 상태가 유효하지 않은 경우 빈 리스트
        }

        // 리스트를 페이지로 변환하여 반환
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredWaitings.size());

        return new PageImpl<>(filteredWaitings.subList(start, end), pageable,
                filteredWaitings.size());
    }

    public long countWaitingInStore(Long storeId) {
        String key = "store:" + storeId + ":waiting";
        ;

        return waitingHashOps.values(key).stream()
                .filter(waiting -> waiting.getWaitingStatus() == WaitingStatus.WAITING)
                .count();
    }

    /**
     * {@link RedissonLockWaitingFacade#updateStatus(UpdateRequest)} 을 사용하세요.
     */
    @DoNotUse(message = "이 메서드를 직접 사용하지 마세요.")
    public void updateStatus(UpdateRequest updateRequest) {
        String key = "store:" + updateRequest.getStoreId() + ":waiting";
        ;

        if (updateRequest.getChangeStatus() == 1) {
            Map<Long, Waiting> waitings = waitingHashOps.entries(key).entrySet().stream()
                    .filter(entry -> entry.getValue().getWaitingStatus() == WaitingStatus.WAITING)
                    .map(entry -> {
                        entry.getValue().setWaitingOrder(entry.getValue().getWaitingOrder() - 1);
                        return entry;
                    })
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            waitingHashOps.putAll(key, waitings);
            decreaseWaitingOrder(updateRequest.getStoreId());

            Waiting waitingToUpdate = waitingHashOps.get(key, updateRequest.getWaitingNumber());
            waitingToUpdate.setVisitedTime(LocalDateTime.now());
            waitingToUpdate.setWaitingStatus(WaitingStatus.VISITED);
            waitingHashOps.put(key, waitingToUpdate.getWaitingNumber(),
                    waitingToUpdate);
        }

        if (updateRequest.getChangeStatus() == 2) {
            // 상태가 WAITING인 Waiting 객체들을 가져와서 waitingOrder가 현재 주문보다 큰 경우만 필터링
            Map<Long, Waiting> waitings = waitingHashOps.entries(key).entrySet().stream()
                    .filter(entry -> (entry.getValue().getWaitingStatus() == WaitingStatus.WAITING) &&
                            (entry.getValue().getWaitingOrder() > updateRequest.getWaitingOrder()))
                    .map(entry -> {
                        entry.getValue().setWaitingOrder(entry.getValue().getWaitingOrder() - 1);
                        return entry;
                    })
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            waitingHashOps.putAll(key, waitings);
            decreaseWaitingOrder(updateRequest.getStoreId());

            Waiting waitingToUpdate = waitingHashOps.get(key, updateRequest.getWaitingNumber());
            waitingToUpdate.setCanceledTime(LocalDateTime.now()); // canceledTime 업데이트
            waitingToUpdate.setWaitingStatus(WaitingStatus.SHOP_CANCELED); // 상태를 CANCELED로 변경
            waitingHashOps.put(key, waitingToUpdate.getWaitingNumber(),
                    waitingToUpdate);
        }
    }

    private Long decreaseWaitingOrder(Long storeId) {
        String waitingOrderKey = "store:" + storeId + ":waitingOrder";
        // Redis에서 waitingOrder 값을 1씩 감소시키고 반환
        return valueOps.increment(waitingOrderKey, -1);
    }
}
