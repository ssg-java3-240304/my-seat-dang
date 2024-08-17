package com.matdang.seatdang.waiting.service;

import com.matdang.seatdang.store.repository.StoreRepository;
import com.matdang.seatdang.store.repository.query.dto.AvailableWaitingTime;
import com.matdang.seatdang.store.repository.query.dto.StoreQueryRepository;
import com.matdang.seatdang.store.vo.Status;
import com.matdang.seatdang.waiting.repository.WaitingRepository;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WaitingSettingService {
    private final StoreQueryRepository storeQueryRepository;
    private final StoreRepository storeRepository;
    private final WaitingRepository waitingRepository;

    public AvailableWaitingTime findAvailableWaitingTime(Long storeId) {
        AvailableWaitingTime findResult = storeQueryRepository.findAvailableWaitingTime(storeId);
        if (findResult.getWaitingOpenTime() == null) {
            return new AvailableWaitingTime(LocalTime.of(0, 0), LocalTime.of(0, 0));
        }

        return findResult;
    }

    public LocalTime findEstimatedWaitingTime(Long storeId) {
        LocalTime findResult = storeQueryRepository.findEstimatedWaitingTime(storeId);
        if (findResult == null) {
            return LocalTime.of(0, 0);
        }

        return findResult;
    }

    @Transactional
    public int changeWaitingStatus(int status, Long storeId) {
        if (status == 1) {
            return storeRepository.updateWaitingStatus(Status.ON, storeId);
        }
        if (status == 2) {
            return storeRepository.updateWaitingStatus(Status.OFF, storeId);
        }
        if (status == 3) {
            int result = storeRepository.updateWaitingStatus(Status.OFF, storeId);
            return waitingRepository.cancelAllWaiting(storeId) + result;
        }

        return 0;
    }
}
