package com.matdang.seatdang.waiting.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.matdang.seatdang.auth.service.AuthService;
import com.matdang.seatdang.common.annotation.DoNotUse;
import com.matdang.seatdang.member.entity.Member;
import com.matdang.seatdang.waiting.dto.WaitingId;
import com.matdang.seatdang.waiting.entity.CustomerInfo;
import com.matdang.seatdang.waiting.entity.Waiting;
import com.matdang.seatdang.waiting.entity.WaitingStatus;
import com.matdang.seatdang.waiting.repository.WaitingRepository;
import com.matdang.seatdang.waiting.repository.query.WaitingQueryRepository;
import com.matdang.seatdang.waiting.repository.query.dto.WaitingInfoProjection;
import com.matdang.seatdang.waiting.service.facade.RedissonLockWaitingCustomerFacade;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WaitingCustomerService {
    private final WaitingRepository waitingRepository;
    private final WaitingQueryRepository waitingQueryRepository;
    private final AuthService authService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    /**
     * {@link RedissonLockWaitingCustomerFacade#createWaiting(Long, Integer)} 을
     * 사용하세요.
     */
    @DoNotUse(message = "이 메서드를 직접 사용하지 마세요.")
    @Transactional
    public WaitingId createWaiting(Long storeId, Integer peopleCount) {
        Member customer = authService.getAuthenticatedMember();

        // Redis에서 최대 waitingNumber 조회 또는 기본값 0 사용
        Long waitingNumber = getNextWaitingNumber(storeId);
        Long waitingOrder = getNextWaitingOrder(storeId);

        // Waiting 객체 생성
        Waiting waiting = Waiting.builder()
                .waitingNumber(waitingNumber)
                .waitingOrder(waitingOrder)
                .storeId(storeId)
                .customerInfo(CustomerInfo.builder()
                        .customerId(customer.getMemberId())
                        .customerPhone(customer.getMemberPhone())
                        .peopleCount(peopleCount)
                        .build())
                .waitingStatus(WaitingStatus.WAITING)
                .visitedTime(null)
                .build();

        try {
            addWaitingToStore(storeId, waiting);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return new WaitingId(waiting.getStoreId(), waiting.getWaitingNumber());
    }

    private Long getNextWaitingNumber(Long storeId) {
        String waitingNumberKey = "waitingNumber:" + storeId;
        // Redis에서 waitingNumber 값을 1씩 증가시키고 반환
        return redisTemplate.opsForValue().increment(waitingNumberKey, 1);
    }

    private Long getNextWaitingOrder(Long storeId) {
        String waitingOrderKey = "waitingOrder:" + storeId;
        // Redis에서 waitingOrder 값을 1씩 증가시키고 반환
        return redisTemplate.opsForValue().increment(waitingOrderKey, 1);
    }

    private void addWaitingToStore(Long storeId, Waiting waiting) throws JsonProcessingException {
        String storeKey = "store:" + storeId;
        Long waitingNumber = waiting.getWaitingNumber(); // 자동으로 증가된 waitingNumber를 가져옴
        String waitingJson = objectMapper.writeValueAsString(waiting);

        // Redis Hash에 추가
        redisTemplate.opsForHash().put(storeKey, waitingNumber.toString(), waitingJson);
    }

    public Map<Long, Waiting> getWaitingsForStore(Long storeId) throws JsonProcessingException {
        String storeKey = "store:" + storeId;

        // Redis Hash에서 모든 필드와 값을 가져옴
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(storeKey);

        // 결과를 변환하여 반환
        Map<Long, Waiting> result = new HashMap<>();
        for (Map.Entry<Object, Object> entry : entries.entrySet()) {
            Long waitingNumber = Long.parseLong((String) entry.getKey());
            String waitingJson = (String) entry.getValue();
            Waiting waiting = objectMapper.readValue(waitingJson, Waiting.class);
            result.put(waitingNumber, waiting);
        }

        return result;
    }

    public Waiting findById(WaitingId waitingId) {
        String storeKey = "store:" + waitingId.getStoreId();
        System.out.println("storeKey = " + storeKey);

        // Redis Hash에서 특정 필드의 값을 가져옴
        String waitingJson = (String) redisTemplate.opsForHash().get(storeKey, waitingId.getWaitingNumber().toString());
        System.out.println("waitingJson = " + waitingJson);

        // JSON 문자열을 Waiting 객체로 역직렬화
        try {
            return objectMapper.readValue(waitingJson, Waiting.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

        /**
         * {@link RedissonLockWaitingCustomerFacade#cancelWaitingByCustomer(Long)} 을
         * 사용하세요.
         */
    @DoNotUse(message = "이 메서드를 직접 사용하지 마세요.")
    @Transactional
    public int cancelWaitingByCustomer(Waiting waiting) {
        int result = waitingRepository.updateWaitingOrderByCancel(waiting.getStoreId(), waiting.getWaitingOrder());
        return waitingRepository.cancelWaitingByCustomer(waiting.getId()) + result;
    }


    @Transactional(readOnly = true)
    public Page<WaitingInfoProjection> showWaiting(int status, int page) {
        PageRequest pageable = PageRequest.of(page, 10);
        Long memberId = authService.getAuthenticatedMember().getMemberId();

        if (status <= 1) {
            return waitingQueryRepository.findUnionAllByCustomerIdAndWaitingStatus(memberId,
                    WaitingStatus.findWaiting(status),
                    pageable);
        }
        if (status == 2) {
            return waitingQueryRepository.findUnionAllByCustomerIdAndCancelStatus(memberId, pageable);
        }

        return Page.empty();
    }
}
