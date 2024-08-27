package com.matdang.seatdang.waiting.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.matdang.seatdang.auth.service.AuthService;
import com.matdang.seatdang.common.annotation.DoNotUse;
import com.matdang.seatdang.member.entity.Member;
import com.matdang.seatdang.store.service.StoreService;
import com.matdang.seatdang.waiting.dto.WaitingId;
import com.matdang.seatdang.waiting.entity.CustomerInfo;
import com.matdang.seatdang.waiting.entity.Waiting;
import com.matdang.seatdang.waiting.service.facade.RedissonLockWaitingCustomerFacade;
import com.matdang.seatdang.waiting.entity.WaitingStatus;
import com.matdang.seatdang.waiting.repository.query.WaitingStorageQueryRepository;
import com.matdang.seatdang.waiting.repository.query.dto.WaitingInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
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
    private final ObjectMapper objectMapper;

    public boolean isWaitingExists(Long storeId) {
        String key = "store:" + storeId;
        Member customer = authService.getAuthenticatedMember();

        if (customer == null) {
            return false;
        }
        return redisTemplate.opsForHash().values(key).stream()
                .map(this::convertStringToWaiting)
                .anyMatch(waiting -> waiting.getCustomerInfo().getCustomerId().equals(customer.getMemberId())
                        && waiting.getWaitingStatus() == WaitingStatus.WAITING);
    }

    public boolean isNotAwaiting(Long storeId, Long waitingNumber) {
        String key = "store:" + storeId;

        return convertStringToWaiting(redisTemplate.opsForHash().get(key, waitingNumber.toString()))
                .getWaitingStatus() != WaitingStatus.WAITING;
    }

    public Boolean isIncorrectWaitingStatus(Long storeId, Long waitingNumber, String status) {
        String key = "store:" + storeId;
        WaitingStatus waitingStatus = convertStringToWaiting(
                redisTemplate.opsForHash().get(key, waitingNumber.toString())).getWaitingStatus();

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

    public Waiting convertStringToWaiting(Object jsonString) {
        try {
            // JSON 문자열을 Waiting 객체로 역직렬화
            return objectMapper.readValue((String) jsonString, Waiting.class);
        } catch (Exception e) {
            e.printStackTrace(); // 예외 처리: 로그 기록
            return null;
        }
    }

    /**
     * {@link RedissonLockWaitingCustomerFacade#createWaiting(Long, Integer)} 을 사용하세요.
     */
    @DoNotUse(message = "이 메서드를 직접 사용하지 마세요.")
    @Transactional
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

        try {
            addWaitingToStore(storeId, waiting);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return new WaitingId(waiting.getStoreId(), waiting.getWaitingNumber());
    }

    // 고객의 storeId에 웨이팅 번호 추가
    private void addWaitingNumber(Long customerId, Long storeId, Long waitingNumber) {
        String key = "customer:" + customerId;
        String field = storeId.toString();

        // 현재 필드의 값을 가져오기
        String currentValue = (String) redisTemplate.opsForHash().get(key, field);

        List<Long> waitingNumbers;
        try {
            if (currentValue == null) {
                waitingNumbers = new ArrayList<>();
            } else {
                waitingNumbers = objectMapper.readValue(currentValue, new TypeReference<List<Long>>() {
                });
            }
            waitingNumbers.add(waitingNumber);

            // JSON 배열로 직렬화하여 Redis에 저장
            redisTemplate.opsForHash().put(key, field, objectMapper.writeValueAsString(waitingNumbers));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 특정 고객의 특정 상점에 대한 웨이팅 번호 리스트 가져오기
    public List<Long> getWaitingNumbersByCustomerAndStore(Long customerId, Long storeId) {
        String key = "customer:" + customerId;
        String field = storeId.toString();
        String value = (String) redisTemplate.opsForHash().get(key, field);

        try {
            if (value != null) {
                return objectMapper.readValue(value, new TypeReference<List<Long>>() {
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    // 특정 고객의 모든 상점과 웨이팅 번호 리스트 가져오기
    public Map<Long, List<Long>> getAllWaitingNumbersByCustomer(Long customerId) {
        String key = "customer:" + customerId;
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);

        try {
            Map<Long, List<Long>> result = new HashMap<>();
            for (Map.Entry<Object, Object> entry : entries.entrySet()) {
                Long storeId = Long.valueOf(entry.getKey().toString());
                List<Long> waitingNumbers = objectMapper.readValue(entry.getValue().toString(),
                        new TypeReference<List<Long>>() {
                        });
                result.put(storeId, waitingNumbers);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }


    private Long getNextWaitingNumber(Long storeId) {
        String waitingNumberKey = "waitingNumber:" + storeId;
        // Redis에서 waitingNumber 값을 1씩 증가시키고 반환
        return redisTemplate.opsForValue().increment(waitingNumberKey, 1);
    }

    private Long getNextWaitingOrder(Long storeId) {
        String waitingOrderKey = "waitingOrder:" + storeId;
        // Redis에서 waitingOrder 값을 1씩 증가시키고 반환
        return redisTemplate.opsForValue().increment(waitingOrderKey, 1);
    }


    private Long getPreviousWaitingOrder(Long storeId) {
        String waitingOrderKey = "waitingOrder:" + storeId;
        // Redis에서 waitingOrder 값을 1씩 감소시키고 반환
        return redisTemplate.opsForValue().increment(waitingOrderKey, -1);
    }

    private void addWaitingToStore(Long storeId, Waiting waiting) throws JsonProcessingException {
        String storeKey = "store:" + storeId;
        Long waitingNumber = waiting.getWaitingNumber(); // 자동으로 증가된 waitingNumber를 가져옴
        String waitingJson = objectMapper.writeValueAsString(waiting);

        // Redis Hash에 추가
        redisTemplate.opsForHash().put(storeKey, waitingNumber.toString(), waitingJson);
    }

    public Map<Long, Waiting> getWaitingsForStore(Long storeId) {
        String storeKey = "store:" + storeId;

        // Redis Hash에서 모든 필드와 값을 가져옴
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(storeKey);

        // 결과를 변환하여 반환
        Map<Long, Waiting> result = new HashMap<>();
        for (Map.Entry<Object, Object> entry : entries.entrySet()) {
            Long waitingNumber = Long.parseLong((String) entry.getKey());
            String waitingJson = (String) entry.getValue();
            Waiting waiting = null;
            try {
                waiting = objectMapper.readValue(waitingJson, Waiting.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            result.put(waitingNumber, waiting);
        }

        return result;
    }

    public Waiting findById(WaitingId waitingId) {
        String storeKey = "store:" + waitingId.getStoreId();
        System.out.println("storeKey = " + storeKey);

        // Redis Hash에서 특정 필드의 값을 가져옴
        String waitingJson = (String) redisTemplate.opsForHash().get(storeKey, waitingId.getWaitingNumber().toString());
        System.out.println("waitingJson = " + waitingJson);

        // JSON 문자열을 Waiting 객체로 역직렬화
        try {
            return objectMapper.readValue(waitingJson, Waiting.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * {@link RedissonLockWaitingCustomerFacade#cancelWaitingByCustomer(Long, Long)} 을 사용하세요.
     */
    @DoNotUse(message = "이 메서드를 직접 사용하지 마세요.")
    @Transactional
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

        Map<String, String> hashEntries = new HashMap<>();
        for (Map.Entry<Long, Waiting> entry : waitings.entrySet()) {
            Long waitingNumber = entry.getKey();
            Waiting waiting = entry.getValue();
            String waitingJson = null;
            try {
                waitingJson = objectMapper.writeValueAsString(waiting);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            hashEntries.put(waitingNumber.toString(), waitingJson);
        }

        // Redis Hash에 저장
        redisTemplate.opsForHash().putAll(storeKey, hashEntries);
    }

//    @Transactional(readOnly = true)
//    public Page<WaitingInfoProjection> showWaiting(int status, int page) {
//        PageRequest pageable = PageRequest.of(page, 10);
//        Long memberId = authService.getAuthenticatedMember().getMemberId();
//
//        if (status <= 1) {
//            return waitingQueryRepository.findUnionAllByCustomerIdAndWaitingStatus(memberId,
//                    WaitingStatus.findWaiting(status),
//                    pageable);
//        }
//        if (status == 2) {
//            return waitingQueryRepository.findUnionAllByCustomerIdAndCancelStatus(memberId, pageable);
//        }
//
//        return Page.empty();
//    }

    public Page<WaitingInfoDto> showHistoryWaiting(int status, int page) {
        PageRequest pageable = PageRequest.of(page, 10);
        Long customerId = authService.getAuthenticatedMember().getMemberId();
        if (status <= 1) {
            return waitingStorageQueryRepository.findAllByCustomerIdAndWaitingStatus(customerId,
                    WaitingStatus.findWaiting(status), pageable);
        }
        if (status == 2) {
            return waitingStorageQueryRepository.findAllByCustomerIdAndCancelStatus(customerId, pageable);
        }

        return Page.empty();
    }

    public Page<WaitingInfoDto> showTodayWaiting(int status, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Long customerId = authService.getAuthenticatedMember().getMemberId();
//

        // 1. 고객의 모든 상점과 대기번호 목록 가져오기
        Map<Long, List<Long>> customerWaitingData = getAllWaitingNumbersByCustomer(customerId);

        System.out.println("customerWaitingData = " + customerWaitingData);

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

        System.out.println("filterStatuses = " + filterStatuses);

        for (Map.Entry<Long, List<Long>> entry : customerWaitingData.entrySet()) {
            Long storeId = entry.getKey();
            List<Long> waitingNumbers = entry.getValue();

            String storeName = storeService.findStoreNameByStoreId(storeId);

            List<String> waitingFields = waitingNumbers.stream()
                    .map(String::valueOf)  // Long 타입의 waitingNumber를 String으로 변환
                    .collect(Collectors.toList());

            // 캐시에서 데이터를 조회합니다.
            HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
            List<String> waitingInfoJsonList = hashOps.multiGet("store:" + storeId, waitingFields);

            System.out.println("waitingInfoJsonList = " + waitingInfoJsonList);

            for (String waitingInfoJson : waitingInfoJsonList) {
                if (waitingInfoJson != null) {
                    try {
                        Waiting waiting = objectMapper.readValue(waitingInfoJson, Waiting.class);

                        if (filterStatuses.contains(waiting.getWaitingStatus())) {
                            WaitingInfoDto dto = new WaitingInfoDto(
                                    waiting.getWaitingNumber(),
                                    storeId,          // storeId 추가
                                    storeName,
                                    waiting.getCustomerInfo().getPeopleCount(),
                                    waiting.getWaitingStatus(),
                                    waiting.getCreatedDate()
                            );
                            allWaitingInfoDtos.add(dto);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        // 3. 필터링된 결과를 createDate 기준으로 내림차순 정렬
        List<WaitingInfoDto> sortedWaitingInfoDtos = allWaitingInfoDtos.stream()
                .sorted(Comparator.comparing(WaitingInfoDto::getCreateDate).reversed())
                .collect(Collectors.toList());

        System.out.println("sortedWaitingInfoDtos = " + sortedWaitingInfoDtos);

        // 4. 페이징 처리
        int total = sortedWaitingInfoDtos.size();
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), total);

        List<WaitingInfoDto> pagedWaitingInfoDtos = sortedWaitingInfoDtos.subList(start, end);

        System.out.println("pagedWaitingInfoDtos = " + pagedWaitingInfoDtos);

        return new PageImpl<>(pagedWaitingInfoDtos, pageable, total);
    }

}
