package com.matdang.seatdang.waiting.service;

import com.matdang.seatdang.auth.service.AuthService;
import com.matdang.seatdang.member.entity.Member;
import com.matdang.seatdang.waiting.entity.CustomerInfo;
import com.matdang.seatdang.waiting.entity.Waiting;
import com.matdang.seatdang.waiting.entity.WaitingStatus;
import com.matdang.seatdang.waiting.repository.WaitingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class WaitingCustomerService {
    private final WaitingRepository waitingRepository;
    private final AuthService authService;

    public Long createWaiting(Long storeId, Integer peopleCount) {
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
    }
}
