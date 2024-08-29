package com.matdang.seatdang.waiting.service;

import com.matdang.seatdang.common.annotation.DoNotUse;
import com.matdang.seatdang.store.repository.StoreRepository;
import com.matdang.seatdang.store.repository.query.dto.AvailableWaitingTime;
import com.matdang.seatdang.store.repository.query.dto.StoreQueryRepository;
import com.matdang.seatdang.store.vo.WaitingStatus;
import com.matdang.seatdang.waiting.repository.WaitingRepository;
import com.matdang.seatdang.waiting.service.facade.RedissonLockWaitingSettingFacade;
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


    /**
     * {@link RedissonLockWaitingSettingFacade#changeWaitingStatus(int, Long)}을 사용하세요.
     */
    @DoNotUse(message = "이 메서드를 직접 사용하지 마세요.")
    @Transactional
    public int changeWaitingStatus(int status, Long storeId) {
        if (status == 1) {
            return storeRepository.updateWaitingStatus(WaitingStatus.OPEN, storeId);
        }
        if (status == 2) {
            return storeRepository.updateWaitingStatus(WaitingStatus.CLOSE, storeId);
        }
        if (status == 3) {
            int result = storeRepository.updateWaitingStatus(WaitingStatus.UNAVAILABLE, storeId);
            return waitingRepository.cancelAllWaiting(storeId) + result;
        }

        return 0;
    }

    /**
     * 상점 생성시, StoreSetting이 생성되면, null값이 들어오지 않아 삭제해도 됨 test code 생략
     */
    public WaitingStatus findWaitingStatus(Long storeId) {
        WaitingStatus findResult = storeQueryRepository.findWaitingStatus(storeId);
        if (findResult == null) {
            return WaitingStatus.UNAVAILABLE;
        }
        return findResult;
    }

    public Integer findWaitingPeopleCount(Long storeId) {
        Integer findResult = storeQueryRepository.findWaitingPeopleCount(storeId);
        if (findResult == null) {
            return 0;
        }

        return findResult;
    }
}
