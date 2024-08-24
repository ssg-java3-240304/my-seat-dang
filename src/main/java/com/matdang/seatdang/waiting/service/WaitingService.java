package com.matdang.seatdang.waiting.service;

import com.matdang.seatdang.common.annotation.DoNotUse;
import com.matdang.seatdang.waiting.dto.UpdateRequest;
import com.matdang.seatdang.waiting.repository.query.WaitingQueryRepository;
import com.matdang.seatdang.waiting.repository.query.dto.WaitingDto;
import com.matdang.seatdang.waiting.entity.WaitingStatus;
import com.matdang.seatdang.waiting.repository.WaitingRepository;
import com.matdang.seatdang.waiting.service.facade.RedissonLockWaitingFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WaitingService {
    private final WaitingRepository waitingRepository;
    private final WaitingQueryRepository waitingQueryRepository;

    public Page<WaitingDto> showWaiting(Long storeId, int status, int page) {
        PageRequest pageable = PageRequest.of(page, 10);

        if (status == 0) {
            return waitingQueryRepository.findAllByWaitingStatusOrderByWaitingOrder(storeId, pageable);
        }
        if (status == 1) {
            return waitingQueryRepository.findAllByStoreIdAndWaitingStatus(storeId,
                    WaitingStatus.findWaiting(status), pageable);
        }
        if (status == 2) {
            return waitingQueryRepository.findAllByCancelStatus(storeId, pageable);
        }

        return Page.empty();
    }

    /**
     * {@link RedissonLockWaitingFacade#updateStatus(UpdateRequest)} 을
     * 사용하세요.
     */
    @DoNotUse(message = "이 메서드를 직접 사용하지 마세요.")
    @Transactional
    public int updateStatus(UpdateRequest updateRequest) {
        if (updateRequest.getChangeStatus() != null) {
            if (updateRequest.getChangeStatus() == 1) {
                int visited = waitingRepository.updateAllWaitingOrderByVisit(updateRequest.getStoreId());
                return waitingRepository.updateStatusByVisit(updateRequest.getId()) + visited;
            }
            if (updateRequest.getChangeStatus() == 2) {
                int canceled = waitingRepository.updateWaitingOrderByCancel(updateRequest.getStoreId(),
                        updateRequest.getWaitingOrder());
                return waitingRepository.updateStatusByShopCancel(updateRequest.getId()) + canceled;
            }
        }

        return 0;
    }
}
