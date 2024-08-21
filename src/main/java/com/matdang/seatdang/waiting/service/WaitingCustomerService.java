package com.matdang.seatdang.waiting.service;

import com.matdang.seatdang.auth.service.AuthService;
import com.matdang.seatdang.member.entity.Member;
import com.matdang.seatdang.waiting.entity.CustomerInfo;
import com.matdang.seatdang.waiting.entity.Waiting;
import com.matdang.seatdang.waiting.entity.WaitingStatus;
import com.matdang.seatdang.waiting.repository.WaitingRepository;
import com.matdang.seatdang.waiting.repository.query.WaitingQueryRepository;
import com.matdang.seatdang.waiting.repository.query.dto.WaitingDto;
import com.matdang.seatdang.waiting.repository.query.dto.WaitingInfoDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WaitingCustomerService {
    private final WaitingRepository waitingRepository;
    private final WaitingQueryRepository waitingQueryRepository;
    private final AuthService authService;


    @Transactional
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

    public Page<WaitingInfoDto> showWaiting(int status, int page) {
        PageRequest pageable = PageRequest.of(page, 10);
        Long memberId = authService.getAuthenticatedMember().getMemberId();
        if (status <= 1) {
            return waitingQueryRepository.findAllByCustomerIdAndWaitingStatus(
                    memberId, WaitingStatus.findWaiting(status), pageable);
        }
        if (status == 2) {
            return waitingQueryRepository.findAllByCustomerIdAndCancelStatus(memberId, pageable);
        }

        return Page.empty();
    }
}
