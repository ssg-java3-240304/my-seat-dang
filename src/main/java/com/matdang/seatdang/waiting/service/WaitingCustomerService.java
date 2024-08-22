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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WaitingCustomerService {
    private final WaitingRepository waitingRepository;
    private final WaitingQueryRepository waitingQueryRepository;
    private final AuthService authService;


    public synchronized Long createWaiting(Long storeId, Integer peopleCount) {
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

    @Transactional
    public int cancelWaitingByCustomer(Long waitingId) {
        Waiting waiting = waitingRepository.findById(waitingId).get();
        int result = waitingRepository.updateWaitingOrderByCancel(waiting.getStoreId(), waiting.getWaitingOrder());

        return waitingRepository.cancelWaitingByCustomer(waitingId) + result;
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
