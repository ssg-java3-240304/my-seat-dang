package com.matdang.seatdang.waiting.service;

import com.matdang.seatdang.store.repository.query.dto.AvailableWaitingTime;
import com.matdang.seatdang.store.repository.query.dto.StoreQueryRepository;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WaitingSettingService {
    private final StoreQueryRepository storeQueryRepository;

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
}
