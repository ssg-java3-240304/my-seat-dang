package com.matdang.seatdang.waiting.service;

import com.matdang.seatdang.auth.service.AuthService;
import com.matdang.seatdang.member.entity.Member;
import com.matdang.seatdang.waiting.entity.CustomerInfo;
import com.matdang.seatdang.waiting.entity.Waiting;
import com.matdang.seatdang.waiting.entity.WaitingStatus;
import com.matdang.seatdang.waiting.repository.WaitingRepository;
import com.matdang.seatdang.waiting.repository.query.WaitingQueryRepository;
import com.matdang.seatdang.waiting.repository.query.dto.WaitingInfoProjection;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WaitingCustomerService {
    private final WaitingRepository waitingRepository;
    private final WaitingQueryRepository waitingQueryRepository;
    private final AuthService authService;
    private final RedissonClient redissonClient; // RedissonClient 추가

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Long createWaiting(Long storeId, Integer peopleCount) {
//        RLock lock = redissonClient.getLock("waitingLock:" + storeId); // 락 키 설정
//        lock.lock(); // 락 획득
//
//        try {
        Member customer = authService.getAuthenticatedMember();

        Waiting waiting = Waiting.builder()
                .waitingNumber(waitingRepository.findMaxWaitingNumberByStoreId(storeId) + 1)
                .waitingOrder(waitingRepository.findMaxWaitingOrderByStoreId(storeId) + 1)
                .storeId(storeId)
                .customerInfo(CustomerInfo.builder()
                        .customerId(customer.getMemberId())
                        .customerPhone(customer.getMemberPhone())
                        .peopleCount(peopleCount)
                        .build())
                .waitingStatus(WaitingStatus.WAITING)
                .visitedTime(null)
                .build();

        return waitingRepository.save(waiting).getId();
//        } finally {
//            lock.unlock(); // 락 해제
//        }
    }

    @Transactional
    public int cancelWaitingByCustomer(Long waitingId) {
        Optional<Waiting> optionalWaiting = waitingRepository.findById(waitingId);
        Waiting waiting = optionalWaiting.orElseThrow(() -> new NoSuchElementException("No waiting found with id: " + waitingId));

//        Waiting waiting = waitingRepository.findById(waitingId).get();
        RLock lock = redissonClient.getLock("waitingLock:" + waiting.getStoreId()); // 락 키 설정
        lock.lock(); // 락 획득

        try {
            int result = waitingRepository.updateWaitingOrderByCancel(waiting.getStoreId(), waiting.getWaitingOrder());

            return waitingRepository.cancelWaitingByCustomer(waitingId) + result;
        } finally {
            lock.unlock();
        }
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
