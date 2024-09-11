package com.matdang.seatdang.waiting.service;

import com.matdang.seatdang.auth.service.AuthService;
import com.matdang.seatdang.common.annotation.DoNotUse;
import com.matdang.seatdang.member.entity.Member;
import com.matdang.seatdang.store.service.StoreService;
import com.matdang.seatdang.waiting.dto.RedisWaitingPage;
import com.matdang.seatdang.waiting.dto.WaitingId;
import com.matdang.seatdang.waiting.entity.CustomerInfo;
import com.matdang.seatdang.waiting.entity.WaitingStatus;
import com.matdang.seatdang.waiting.redis.Waiting;
import com.matdang.seatdang.waiting.redis.WaitingNumbers;
import com.matdang.seatdang.waiting.repository.WaitingStorageRepository;
import com.matdang.seatdang.waiting.repository.query.WaitingStorageQueryRepository;
import com.matdang.seatdang.waiting.repository.query.dto.WaitingInfoDto;
import com.matdang.seatdang.waiting.service.facade.RedissonLockWaitingCustomerFacade;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WaitingCustomerService {
    private final WaitingStorageQueryRepository waitingStorageQueryRepository;
    private final StoreService storeService;
    private final AuthService authService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisTemplate<String, Waiting> waitingRedisTemplate;
    private final RedisTemplate<String, WaitingNumbers> waitingNumbersRedisTemplate;
    private final WaitingStorageRepository waitingStorageRepository;

    private HashOperations<String, Long, Waiting> waitingHashOps;
    private HashOperations<String, Long, WaitingNumbers> waitingNumberHashOps;
    private ValueOperations<String, Object> valueOps;

    @PostConstruct
    public void init() {
        this.valueOps = redisTemplate.opsForValue();
        this.waitingHashOps = waitingRedisTemplate.opsForHash();
        this.waitingNumberHashOps = waitingNumbersRedisTemplate.opsForHash();
    }

    @Transactional(readOnly = true)
    public boolean isWaitingExists(Long storeId) {
        String key = "store:" + storeId;
        Member customer = authService.getAuthenticatedMember();

        if (customer == null) {
            return false;
        }

        return waitingHashOps.values(key).stream()
                .anyMatch(waiting -> waiting.getCustomerInfo().getCustomerId().equals(customer.getMemberId())
                        && waiting.getWaitingStatus() == WaitingStatus.WAITING);
    }

    public boolean isNotAwaiting(Long storeId, Long waitingNumber) {
        String key = "store:" + storeId;

        return waitingHashOps.get(key, waitingNumber.toString()).getWaitingStatus() != WaitingStatus.WAITING;
    }

    @Transactional(readOnly = true)
    public Boolean isIncorrectWaitingStatus(Long storeId, Long waitingNumber, String status, String when) {
        WaitingStatus waitingStatus = null;
        if (when.equals("today")) {
            String key = "store:" + storeId;
            waitingStatus = waitingHashOps.get(key, waitingNumber.toString()).getWaitingStatus();
        }
        if (when.equals("history")) {
            waitingStatus = waitingStorageRepository.findByStoreIdAndWaitingNumber(storeId,
                    waitingNumber).getWaitingStatus();
        }

        if (status.equals("awaiting")) {
            return waitingStatus != WaitingStatus.WAITING;
        }
        if (status.equals("visited")) {
            return waitingStatus != WaitingStatus.VISITED;
        }
        if (status.equals("canceled")) {
            return (waitingStatus != WaitingStatus.SHOP_CANCELED) &&
                    (waitingStatus != WaitingStatus.CUSTOMER_CANCELED) &&
                    (waitingStatus != WaitingStatus.NO_SHOW);
        }

        return null;
    }

    /**
     * {@link RedissonLockWaitingCustomerFacade#createWaiting(Long, Integer)} 을 사용하세요.
     */
    @DoNotUse(message = "이 메서드를 직접 사용하지 마세요.")
    @Transactional(readOnly = true)
    public WaitingId createWaiting(Long storeId, Integer peopleCount) {
        Member customer = authService.getAuthenticatedMember();

        // Redis에서 최대 waitingNumber 조회 또는 기본값 0 사용
        Long waitingNumber = getNextWaitingNumber(storeId);
        Long waitingOrder = getNextWaitingOrder(storeId);

        // 고객의 storeId에 웨이팅 번호 추가
        addWaitingNumber(customer.getMemberId(), storeId, waitingNumber);

        // Waiting 객체 생성
        Waiting waiting = Waiting.builder()
                .waitingNumber(waitingNumber)
                .waitingOrder(waitingOrder)
                .storeId(storeId)
                .createdDate(LocalDateTime.now())
                .customerInfo(CustomerInfo.builder()
                        .customerId(customer.getMemberId())
                        .customerPhone(customer.getMemberPhone())
                        .peopleCount(peopleCount)
                        .build())
                .waitingStatus(WaitingStatus.WAITING)
                .visitedTime(null)
                .build();

        addWaitingToStore(storeId, waiting);

        return new WaitingId(waiting.getStoreId(), waiting.getWaitingNumber());
    }

    // 고객의 storeId에 웨이팅 번호 추가
    private void addWaitingNumber(Long customerId, Long storeId, Long waitingNumber) {
        String key = "customer:" + customerId;

        // 현재 필드의 값을 가져오기

        WaitingNumbers currentValue = waitingNumberHashOps.get(key, storeId);

        if (currentValue == null) {
            currentValue = new WaitingNumbers();
        }
        currentValue.getWaitingNumbers().add(waitingNumber);

        waitingNumberHashOps.put(key, storeId, currentValue);
    }

    // 특정 고객의 특정 상점에 대한 웨이팅 번호 리스트 가져오기
    public List<Long> getWaitingNumbersByCustomerAndStore(Long customerId, Long storeId) {
        String key = "customer:" + customerId;
        WaitingNumbers value = waitingNumberHashOps.get(key, storeId);

        if (value != null) {
            return value.getWaitingNumbers();
        }
        return new ArrayList<>();
    }

    // 특정 고객의 모든 상점과 웨이팅 번호 리스트 가져오기
    public Map<Long, WaitingNumbers> getAllWaitingNumbersByCustomer(Long customerId) {
        String key = "customer:" + customerId;
        return waitingNumberHashOps.entries(key);
    }

    private Long getNextWaitingNumber(Long storeId) {
        String waitingNumberKey = "waitingNumber:" + storeId;
        // Redis에서 waitingNumber 값을 1씩 증가시키고 반환
        return valueOps.increment(waitingNumberKey, 1);
    }

    private Long getNextWaitingOrder(Long storeId) {
        String waitingOrderKey = "waitingOrder:" + storeId;
        return valueOps.increment(waitingOrderKey, 1);
    }


    private Long getPreviousWaitingOrder(Long storeId) {
        String waitingOrderKey = "waitingOrder:" + storeId;
        return valueOps.increment(waitingOrderKey, -1);
    }

    private void addWaitingToStore(Long storeId, Waiting waiting) {
        String storeKey = "store:" + storeId;
        waitingHashOps.put(storeKey, waiting.getWaitingNumber(), waiting);
    }

    public Map<Long, Waiting> getWaitingsForStore(Long storeId) {
        String storeKey = "store:" + storeId;
        return waitingHashOps.entries(storeKey);
    }

    public Waiting findById(WaitingId waitingId) {
        String storeKey = "store:" + waitingId.getStoreId();
        return waitingHashOps.get(storeKey, waitingId.getWaitingNumber());
    }

    /**
     * {@link RedissonLockWaitingCustomerFacade#cancelWaitingByCustomer(Long, Long)} 을 사용하세요.
     */
    @DoNotUse(message = "이 메서드를 직접 사용하지 마세요.")
    public void cancelWaitingByCustomer(Long waitingNumber, Long storeId) {
        getPreviousWaitingOrder(storeId);
        Map<Long, Waiting> waitings = getWaitingsForStore(storeId);
        Waiting waiting = findById(new WaitingId(storeId, waitingNumber));

        for (Long l : waitings.keySet()) {
            if (waitingNumber.equals(waitings.get(l).getWaitingNumber())) {
                waitings.get(l).setWaitingStatus(WaitingStatus.CUSTOMER_CANCELED);
                waitings.get(l).setCanceledTime(LocalDateTime.now());
            }
            if (waiting.getWaitingOrder() < waitings.get(l).getWaitingOrder()) {
                waitings.get(l).setWaitingOrder(waitings.get(l).getWaitingOrder() - 1);
            }
        }

        saveWaitingsToRedis(waitings, waiting.getStoreId());
    }

    public void saveWaitingsToRedis(Map<Long, Waiting> waitings, Long storeId) {
        String storeKey = "store:" + storeId;

        waitingHashOps.putAll(storeKey, waitings);
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "showHistoryWaiting", key = "'historyWaiting:customer'+#customerId+ ':status:' +#status +':page:' + #page ", cacheManager = "waitingStorageCacheManager")
    public RedisWaitingPage showHistoryWaiting(Long customerId, int status, int page) {
        PageRequest pageable = PageRequest.of(page, 10);
        Page<WaitingInfoDto> resultPage;
        if (status <= 1) {
            resultPage = waitingStorageQueryRepository.findAllByCustomerIdAndWaitingStatus(customerId,
                    WaitingStatus.findWaiting(status), pageable);
        } else if (status == 2) {
            resultPage = waitingStorageQueryRepository.findAllByCustomerIdAndCancelStatus(customerId, pageable);
        } else {
            resultPage = Page.empty();
        }
        return new RedisWaitingPage(resultPage);
    }

    @Transactional(readOnly = true)
    public Page<WaitingInfoDto> showTodayWaiting(int status, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Long customerId = authService.getAuthenticatedMember().getMemberId();

        // 1. 고객의 모든 상점과 대기번호 목록 가져오기
        Map<Long, WaitingNumbers> customerWaitingData = getAllWaitingNumbersByCustomer(customerId);

        // 2. 각 상점의 대기번호에 대해 실제 waiting 정보 조회 및 필터링
        List<WaitingInfoDto> allWaitingInfoDtos = new ArrayList<>();

        // status 필터 정의
        WaitingStatus filterStatus = WaitingStatus.findWaiting(status);
        Set<WaitingStatus> filterStatuses = new HashSet<>();

        if (status <= 1) {
            filterStatuses.add(filterStatus);
        } else if (status == 2) {
            filterStatuses.add(WaitingStatus.SHOP_CANCELED);
            filterStatuses.add(WaitingStatus.NO_SHOW);
            filterStatuses.add(WaitingStatus.CUSTOMER_CANCELED);
        }

        for (Map.Entry<Long, WaitingNumbers> entry : customerWaitingData.entrySet()) {
            Long storeId = entry.getKey();
            List<Long> waitingNumbers = entry.getValue().getWaitingNumbers();

            String storeName = storeService.findStoreNameByStoreId(storeId);

            List<Waiting> waitingInfoJsonList = waitingHashOps.multiGet("store:" + storeId, waitingNumbers);

            for (Waiting waiting : waitingInfoJsonList) {
                if (filterStatuses.contains(waiting.getWaitingStatus())) {
                    allWaitingInfoDtos.add(new WaitingInfoDto(waiting, storeName));
                }
            }
        }

        // 3. 필터링된 결과를 createDate 기준으로 내림차순 정렬
        List<WaitingInfoDto> sortedWaitingInfoDtos = allWaitingInfoDtos.stream()
                .sorted(Comparator.comparing(WaitingInfoDto::getCreateDate).reversed())
                .collect(Collectors.toList());

        // 4. 페이징 처리
        int total = sortedWaitingInfoDtos.size();
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), total);

        List<WaitingInfoDto> pagedWaitingInfoDtos = sortedWaitingInfoDtos.subList(start, end);

        return new PageImpl<>(pagedWaitingInfoDtos, pageable, total);
    }
}
