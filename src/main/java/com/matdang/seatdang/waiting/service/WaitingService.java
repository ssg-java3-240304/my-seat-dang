package com.matdang.seatdang.waiting.service;

import com.matdang.seatdang.waiting.entity.Waiting;
import com.matdang.seatdang.waiting.entity.WaitingStatus;
import com.matdang.seatdang.waiting.repository.WaitingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class WaitingService {
    private final WaitingRepository waitingRepository;

    public List<Waiting> showWaiting(Long storeId, int status) {
        if (status == 2) {
            return waitingRepository.findAllByCancelStatus(storeId);
        }

        List<Waiting> waitings = waitingRepository.findAllByStoreIdAndWaitingStatus(storeId, WaitingStatus.findWaiting(status));
        return waitings;
    }


}
