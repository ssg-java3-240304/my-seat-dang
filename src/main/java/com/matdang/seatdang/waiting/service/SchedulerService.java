package com.matdang.seatdang.waiting.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matdang.seatdang.store.entity.Store;
import com.matdang.seatdang.store.repository.StoreRepository;
import com.matdang.seatdang.store.vo.WaitingStatus;
import com.matdang.seatdang.waiting.redis.Waiting;
import com.matdang.seatdang.waiting.entity.WaitingStorage;
import com.matdang.seatdang.waiting.repository.WaitingStorageRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class SchedulerService {
    private final StoreRepository storeRepository;
    private final WaitingStorageRepository waitingStorageRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    // 매일 오전 5시에 실행
    @Scheduled(cron = "0 0 5 * * ?")
    public void relocateWaitingData() {
        log.info("[Relocate Waiting Data] This runs at 5:00 AM every day.");
        List<Store> stores = storeRepository.findAll();

        for (Store store : stores) {
            if (store.getStoreSetting().getWaitingStatus() == WaitingStatus.CLOSE ||
                    store.getStoreSetting().getWaitingStatus() == WaitingStatus.UNAVAILABLE) {
                String key = "store:"+ store.getStoreId();
                List<WaitingStorage> waitings = redisTemplate.opsForHash().values(key).stream()
                        .map(this::convertStringToWaiting) // JSON 문자열을 Waiting 객체로 변환
                        .map(Waiting::convertToWaitingStorage) // Waiting 객체를 WaitingStorage 객체로 변환
                        .toList();


                List<WaitingStorage> savedAll = waitingStorageRepository.saveAll(waitings);
                log.info("=== save storeId = {}, size = {} ===", store.getStoreId(), savedAll.size());

                redisTemplate.delete("waitingOrder:" + store.getStoreId());
                redisTemplate.delete("waitingNumber:" + store.getStoreId());
                redisTemplate.delete(key);
                log.info("=== Redis data deleted for storeId = {} ===", store.getStoreId());
            }
        }
    }

    public Waiting convertStringToWaiting(Object jsonString) {
        try {
            // JSON 문자열을 Waiting 객체로 역직렬화
            return objectMapper.readValue((String) jsonString, Waiting.class);
        } catch (Exception e) {
            e.printStackTrace(); // 예외 처리: 로그 기록
            return null;
        }
    }

    // 10분 마다 실행
    @Scheduled(cron = "0 0/18 * * * ?")
    public void closeWaiting() {
        log.info("[Close Waiting] This runs every 10 minutes");
        LocalTime current = LocalTime.now();
        List<Store> stores = storeRepository.findAll();
        for (Store store : stores) {
            LocalTime closeTime = store.getStoreSetting().getWaitingTime().getWaitingCloseTime();

            if ((closeTime.getHour() < 12 && current.getHour() < 12) || (current.getHour() >= 12 && closeTime.getHour() >= 12)) {
                if ((store.getStoreSetting().getWaitingStatus() == WaitingStatus.OPEN) &&
                        (Duration.between(current, closeTime)
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
}
