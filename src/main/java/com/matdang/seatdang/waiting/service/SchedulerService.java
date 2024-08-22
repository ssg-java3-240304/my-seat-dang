package com.matdang.seatdang.waiting.service;

import com.matdang.seatdang.store.entity.Store;
import com.matdang.seatdang.store.repository.StoreRepository;
import com.matdang.seatdang.store.repository.query.dto.StoreQueryRepository;
import com.matdang.seatdang.store.vo.WaitingStatus;
import com.matdang.seatdang.waiting.entity.Waiting;
import com.matdang.seatdang.waiting.entity.WaitingStorage;
import com.matdang.seatdang.waiting.repository.WaitingRepository;
import com.matdang.seatdang.waiting.repository.WaitingStorageRepository;
import com.matdang.seatdang.waiting.repository.query.WaitingQueryRepository;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class SchedulerService {
    private final StoreRepository storeRepository;
    private final WaitingRepository waitingRepository;
    private final WaitingQueryRepository waitingQueryRepository;
    private final WaitingStorageRepository waitingStorageRepository;

    // 매일 오전 5시에 실행
    @Scheduled(cron = "0 30 * * * ?")
    public void relocateWaitingData() {
        log.info("[Relocate Waiting Data] This runs at 5:00 AM every day.");

        List<Store> stores = storeRepository.findAll();
        for (Store store : stores) {
            if (store.getStoreSetting().getWaitingStatus() == WaitingStatus.CLOSE ||
                    store.getStoreSetting().getWaitingStatus() == WaitingStatus.UNAVAILABLE) {
                List<WaitingStorage> waitings = waitingQueryRepository.findAllByStoreId(store.getStoreId());

                List<WaitingStorage> savedAll = waitingStorageRepository.saveAll(waitings);
                log.info("=== save storeId = {}, size = {} ===", store.getStoreId(), savedAll.size());

                waitingRepository.deleteAllByStoreId(store.getStoreId());
                if (waitingRepository.findAllByStoreId(store.getStoreId()).isEmpty()) {
                    log.info("=== delete Waitings ===");
                }

            }

        }

    }

    // 10분 마다 실행
    @Scheduled(cron = "0 0/10 * * * ?")
    public void closeWaiting() {
        log.info("[Close Waiting] This runs every 10 minutes");

        List<Store> stores = storeRepository.findAll();
        for (Store store : stores) {
            if ((store.getStoreSetting().getWaitingStatus() == WaitingStatus.OPEN) &&
                    (Duration.between(LocalTime.now(), store.getStoreSetting().getWaitingTime().getWaitingCloseTime())
                            .toMinutes() <= 0)) {

                int result = storeRepository.updateWaitingStatus(WaitingStatus.CLOSE, store.getStoreId());
                if (result == 1) {
                    log.info("=== Close Waiting ===");
                    log.info("storeId ={}", store.getStoreId());
                    log.info("=====================");
                } else {
                    log.error("=== Fail Close Waiting ===");
                }
            }

        }

    }
}
