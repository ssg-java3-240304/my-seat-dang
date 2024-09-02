package com.matdang.seatdang.waiting.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.matdang.seatdang.common.annotation.DoNotUse;
import com.matdang.seatdang.waiting.dto.UpdateRequest;
import com.matdang.seatdang.waiting.redis.Waiting;
import com.matdang.seatdang.waiting.repository.query.dto.WaitingDto;
import com.matdang.seatdang.waiting.entity.WaitingStatus;
import com.matdang.seatdang.waiting.service.facade.RedissonLockWaitingFacade;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WaitingService {
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Object> redisTemplate;

//    public Page<WaitingDto> showWaiting(Long storeId, int status, int page) {
//        PageRequest pageable = PageRequest.of(page, 10);
//
//        if (status == 0) {
//            return waitingQueryRepository.findAllByWaitingStatusOrderByWaitingOrder(storeId, pageable);
//        }
//        if (status == 1) {
//            return waitingQueryRepository.findAllByStoreIdAndWaitingStatus(storeId,
//                    WaitingStatus.findWaiting(status), pageable);
//        }
//        if (status == 2) {
//            return waitingQueryRepository.findAllByCancelStatus(storeId, pageable);
//        }
//
//        return Page.empty();
//    }


    public Page<WaitingDto> showWaiting(Long storeId, int status, int page) {
        String key = "store:" + storeId;
        PageRequest pageable = PageRequest.of(page, 10);
        List<Object> values = redisTemplate.opsForHash().values(key);

        List<WaitingDto> waitingDtos = redisTemplate.opsForHash().values(key).stream()
                .map(this::convertStringToWaiting)
                .filter(Objects::nonNull)
                .map(WaitingDto::create)
                .toList();
        System.out.println("waitingDtos = " + waitingDtos);

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
        String key = "store:" + storeId;

        return redisTemplate.opsForHash().values(key).stream()
                .map(this::convertStringToWaiting)
                .filter(waiting -> waiting.getWaitingStatus() == WaitingStatus.WAITING)
                .count();
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

//    @Transactional
//    public int updateStatus(UpdateRequest updateRequest) {
//        if (updateRequest.getChangeStatus() != null) {
//            if (updateRequest.getChangeStatus() == 1) {
//                int visited = waitingRepository.updateAllWaitingOrderByVisit(updateRequest.getStoreId());
//                return waitingRepository.updateStatusByVisit(updateRequest.getWaitingNumber()) + visited;
//            }
//            if (updateRequest.getChangeStatus() == 2) {
//                int canceled = waitingRepository.updateWaitingOrderByCancel(updateRequest.getStoreId(),
//                        updateRequest.getWaitingOrder());
//                return waitingRepository.updateStatusByShopCancel(updateRequest.getWaitingNumber()) + canceled;
//            }
//        }
//
//        return 0;
//    }

    /**
     * {@link RedissonLockWaitingFacade#updateStatus(UpdateRequest)} 을 사용하세요.
     */
    @DoNotUse(message = "이 메서드를 직접 사용하지 마세요.")
    @Transactional
    public void updateStatus(UpdateRequest updateRequest) {
        String key = "store:" + updateRequest.getStoreId();

        if (updateRequest.getChangeStatus() == 1) {
            List<Waiting> waitingList = redisTemplate.opsForHash().values(key).stream()
                    .map(this::convertStringToWaiting) // JSON 문자열을 Waiting 객체로 변환
                    .filter(waiting -> waiting.getWaitingStatus() == WaitingStatus.WAITING)
                    .toList();

            Map<String, String> updatedWaitings = new HashMap<>();
            for (Waiting waiting : waitingList) {
                waiting.setWaitingOrder(waiting.getWaitingOrder() - 1);
                try {
                    updatedWaitings.put(waiting.getWaitingNumber().toString(),
                            objectMapper.writeValueAsString(waiting));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }

            }
            redisTemplate.opsForHash().putAll(key, updatedWaitings);
            decreaseWaitingOrder(updateRequest.getStoreId());

            Waiting waitingToUpdate = null;
            try {
                waitingToUpdate = objectMapper.readValue(
                        (String) redisTemplate.opsForHash()
                                .get(key, updateRequest.getWaitingNumber().toString()), Waiting.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            waitingToUpdate.setVisitedTime(LocalDateTime.now());
            waitingToUpdate.setWaitingStatus(WaitingStatus.VISITED);
            try {
                redisTemplate.opsForHash().put(key, waitingToUpdate.getWaitingNumber().toString(),
                        objectMapper.writeValueAsString(waitingToUpdate));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

        }

        if (updateRequest.getChangeStatus() == 2) {
            // 상태가 WAITING인 Waiting 객체들을 가져와서 waitingOrder가 현재 주문보다 큰 경우만 필터링
            List<Waiting> waitingList = redisTemplate.opsForHash().values(key).stream()
                    .map(this::convertStringToWaiting) // JSON 문자열을 Waiting 객체로 변환
                    .filter(waiting -> (waiting.getWaitingStatus() == WaitingStatus.WAITING) &&
                            (waiting.getWaitingOrder() > updateRequest.getWaitingOrder())) // 주문보다 큰 것만 필터링
                    .toList();

            Map<String, String> updatedWaitings = new HashMap<>();
            for (Waiting waiting : waitingList) {
                waiting.setWaitingOrder(waiting.getWaitingOrder() - 1);
                try {
                    updatedWaitings.put(waiting.getWaitingNumber().toString(),
                            objectMapper.writeValueAsString(waiting));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
            redisTemplate.opsForHash().putAll(key, updatedWaitings);
            decreaseWaitingOrder(updateRequest.getStoreId());

            Waiting waitingToUpdate = null;
            try {
                waitingToUpdate = objectMapper.readValue(
                        (String) redisTemplate.opsForHash()
                                .get(key, updateRequest.getWaitingNumber().toString()), Waiting.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            waitingToUpdate.setCanceledTime(LocalDateTime.now()); // canceledTime 업데이트
            waitingToUpdate.setWaitingStatus(WaitingStatus.SHOP_CANCELED); // 상태를 CANCELED로 변경
            try {
                redisTemplate.opsForHash().put(key, waitingToUpdate.getWaitingNumber().toString(),
                        objectMapper.writeValueAsString(waitingToUpdate));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Long decreaseWaitingOrder(Long storeId) {
        String waitingOrderKey = "waitingOrder:" + storeId;
        // Redis에서 waitingOrder 값을 1씩 감소시키고 반환
        return redisTemplate.opsForValue().increment(waitingOrderKey, -1);
    }

}
