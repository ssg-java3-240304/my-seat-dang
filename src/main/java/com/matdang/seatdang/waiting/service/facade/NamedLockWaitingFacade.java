package com.matdang.seatdang.waiting.service.facade;

import com.matdang.seatdang.waiting.repository.LockRepository;
import com.matdang.seatdang.waiting.service.WaitingCustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Deprecated
@Component
@RequiredArgsConstructor
public class NamedLockWaitingFacade {
    private final LockRepository lockRepository;
    private final WaitingCustomerService waitingCustomerService;


    @Transactional
    public void createWaiting(Long storeId, Integer peopleCount) {
        try {
            lockRepository.getLock(storeId.toString());
            waitingCustomerService.createWaiting(storeId, peopleCount);
        }finally {
            lockRepository.releaseLock(storeId.toString());
        }

    }
}
