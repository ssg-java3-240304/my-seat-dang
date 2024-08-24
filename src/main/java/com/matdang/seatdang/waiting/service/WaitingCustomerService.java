package com.matdang.seatdang.waiting.service;

import com.matdang.seatdang.auth.service.AuthService;
import com.matdang.seatdang.common.annotation.DoNotUse;
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
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WaitingCustomerService {
    private final WaitingRepository waitingRepository;
    private final WaitingQueryRepository waitingQueryRepository;
    private final AuthService authService;

    /**
     * {@link com.matdang.seatdang.waiting.service.facade.RedissonLockWaitingFacade#createWaiting(Long, Integer)} 을
     * 사용하세요.
     */
    @DoNotUse(message = "이 메서드를 직접 사용하지 마세요.")
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


    /**
     * {@link com.matdang.seatdang.waiting.service.facade.RedissonLockWaitingFacade#cancelWaitingByCustomer(Long)} 을
     * 사용하세요.
     */
    @DoNotUse(message = "이 메서드를 직접 사용하지 마세요.")
    @Transactional
    public int cancelWaitingByCustomer(Long waitingId) {
        Optional<Waiting> optionalWaiting = waitingRepository.findById(waitingId);
        Waiting waiting = optionalWaiting.orElseThrow(
                () -> new NoSuchElementException("No waiting found with id: " + waitingId));

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
