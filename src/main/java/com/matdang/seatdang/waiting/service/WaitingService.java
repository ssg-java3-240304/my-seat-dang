package com.matdang.seatdang.waiting.service;

import com.matdang.seatdang.waiting.dto.UpdateRequest;
import com.matdang.seatdang.waiting.repository.query.WaitingQueryRepository;
import com.matdang.seatdang.waiting.repository.query.dto.WaitingDto;
import com.matdang.seatdang.waiting.entity.WaitingStatus;
import com.matdang.seatdang.waiting.repository.WaitingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WaitingService {
    private final WaitingRepository waitingRepository;
    private final WaitingQueryRepository waitingQueryRepository;

    public List<WaitingDto> showWaiting(Long storeId, int status) {
        if (status == 0) {
            return waitingQueryRepository.findAllByWaitingStatusOrderByWaitingOrder(storeId);
        }
        if (status == 2) {
            return waitingQueryRepository.findAllByCancelStatus(storeId);
        }
        List<WaitingDto> waitings = waitingQueryRepository.findAllByStoreIdAndWaitingStatus(storeId,
                WaitingStatus.findWaiting(status));
        return waitings;
    }

    @Transactional
    public int updateStatus(UpdateRequest updateRequest) {
        if (updateRequest.getChangeStatus() != null) {
            int visited = 0;
            int canceled = 0;
            if (updateRequest.getChangeStatus() == 1) {
                visited = waitingRepository.updateAllWaitingNumberByVisit(updateRequest.getStoreId());
            }
            if (updateRequest.getChangeStatus() == 2) {
                canceled = waitingRepository.updateWaitingNumberByCancel(updateRequest.getStoreId(),
                        updateRequest.getWaitingNumber());
            }

            return waitingRepository.updateStatus(WaitingStatus.findWaiting(updateRequest.getChangeStatus()),
                    updateRequest.getId()) + visited + canceled;
        }

        return 0;
    }


}
