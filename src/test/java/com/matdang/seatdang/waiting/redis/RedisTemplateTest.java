package com.matdang.seatdang.waiting.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.matdang.seatdang.waiting.entity.CustomerInfo;
import com.matdang.seatdang.waiting.entity.WaitingStatus;
import com.matdang.seatdang.waiting.repository.query.dto.WaitingDto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RedisTemplateTest {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private RedisTemplate<String, Waiting> waitingRedisTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RedisTemplate<String, WaitingNumbers> waitingNumbersRedisTemplate;

    @Test
    @DisplayName("Waiting Redis 데이터 생성 - ObjectMapper 사용o")
    void createByRedisTemplate() {
        // given
        Waiting waiting = Waiting.builder()
                .waitingNumber(1L)
                .waitingOrder(1L)
                .storeId(1L)
                .createdDate(LocalDateTime.now())
                .customerInfo(CustomerInfo.builder()
                        .customerId(1L)
                        .customerPhone("010-1234-1234")
                        .peopleCount(1)
                        .build())
                .waitingStatus(WaitingStatus.WAITING)
                .visitedTime(null)
                .build();

        // when

        LocalTime start = LocalTime.now();
        for (int i = 0; i < 1000; i++) {
            String waitingJson = null;
            // then
            try {
                waitingJson = objectMapper.writeValueAsString(waiting);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            redisTemplate.opsForHash().put("store:1", "1", waitingJson);
        }

        LocalTime end = LocalTime.now();

        System.out.println(" elapsed time = " + Duration.between(start, end).toMillis());
        // 665ms
    }

    @Test
    @DisplayName("Waiting Redis 데이터 생성 - ObjectMapper 사용x")
    void createByWaitingRedisTemplate() {
        // given
        Waiting waiting = Waiting.builder()
                .waitingNumber(1L)
                .waitingOrder(1L)
                .storeId(1L)
                .createdDate(LocalDateTime.now())
                .customerInfo(CustomerInfo.builder()
                        .customerId(1L)
                        .customerPhone("010-1234-1234")
                        .peopleCount(1)
                        .build())
                .waitingStatus(WaitingStatus.WAITING)
                .visitedTime(null)
                .build();
        // when
        // then
        LocalTime start = LocalTime.now();
        for (int i = 0; i < 1000; i++) {
            waitingRedisTemplate.opsForHash().put("store:1", 1L, waiting);
        }
        LocalTime end = LocalTime.now();

        System.out.println(" elapsed time = " + Duration.between(start, end).toMillis());
        // 682ms
    }

    @Test
    @DisplayName("Waiting Redis 데이터 조회")
    void findByWaitingRedisTemplate() {
        // given
        Waiting waiting = Waiting.builder()
                .waitingNumber(1L)
                .waitingOrder(1L)
                .storeId(1L)
                .createdDate(LocalDateTime.now())
                .customerInfo(CustomerInfo.builder()
                        .customerId(1L)
                        .customerPhone("010-1234-1234")
                        .peopleCount(1)
                        .build())
                .waitingStatus(WaitingStatus.WAITING)
                .visitedTime(null)
                .build();
        waitingRedisTemplate.opsForHash().put("store:1", 1L, waiting);

        // when
        LocalTime start = LocalTime.now();
        Waiting findResult = null;
        for (int i = 0; i < 1000; i++) {
            findResult = (Waiting) waitingRedisTemplate.opsForHash().get("store:1", 1L);
        }
        LocalTime end = LocalTime.now();
        // then
        assertThat(findResult.getWaitingStatus()).isEqualTo(waiting.getWaitingStatus());

        System.out.println(" elapsed time = " + Duration.between(start, end).toMillis());
        // 200 ~ 300ms
    }

    @Test
    @DisplayName("Waiting Redis 데이터 조회 - 캐스팅 x")
    void findByWaitingRedisTemplateAndNotCasting() {
        // given
        Waiting waiting = Waiting.builder()
                .waitingNumber(1L)
                .waitingOrder(1L)
                .storeId(1L)
                .createdDate(LocalDateTime.now())
                .customerInfo(CustomerInfo.builder()
                        .customerId(1L)
                        .customerPhone("010-1234-1234")
                        .peopleCount(1)
                        .build())
                .waitingStatus(WaitingStatus.WAITING)
                .visitedTime(null)
                .build();
        waitingRedisTemplate.opsForHash().put("store:1", 1L, waiting);

        // when
        LocalTime start = LocalTime.now();
        Waiting findResult = null;
        HashOperations<String, Long, Waiting> hashOperations = waitingRedisTemplate.opsForHash();
        for (int i = 0; i < 1000; i++) {
            findResult = hashOperations.get("store:1", 1L);
        }
        LocalTime end = LocalTime.now();
        // then
        assertThat(findResult.getWaitingStatus()).isEqualTo(waiting.getWaitingStatus());

        System.out.println(" elapsed time = " + Duration.between(start, end).toMillis());
        // 200 ~ 300ms
    }

    @Test
    @DisplayName("Waiting Redis 데이터 조회 - objectMapper 사용 o")
    void findByRedisTemplate() {
        // given
        Waiting waiting = Waiting.builder()
                .waitingNumber(1L)
                .waitingOrder(1L)
                .storeId(1L)
                .createdDate(LocalDateTime.now())
                .customerInfo(CustomerInfo.builder()
                        .customerId(1L)
                        .customerPhone("010-1234-1234")
                        .peopleCount(1)
                        .build())
                .waitingStatus(WaitingStatus.WAITING)
                .visitedTime(null)
                .build();
        waitingRedisTemplate.opsForHash().put("store:1", 1L, waiting);

        // when
        LocalTime start = LocalTime.now();
        Waiting findResult = null;
        for (int i = 0; i < 1000; i++) {
            try {
                findResult = objectMapper.readValue((String) redisTemplate.opsForHash().get("store:1", "1"),
                        Waiting.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        LocalTime end = LocalTime.now();
        // then
        assertThat(findResult.getWaitingStatus()).isEqualTo(waiting.getWaitingStatus());

        System.out.println(" elapsed time = " + Duration.between(start, end).toMillis());
        // 200 ~300ms
    }

    @Test
    @DisplayName("Waiting Redis 데이터 여러건 조회 - objectMapper 사용 o")
    void findAllByRedisTemplate() {
        // given
        Waiting waiting = Waiting.builder()
                .waitingNumber(1L)
                .waitingOrder(1L)
                .storeId(1L)
                .createdDate(LocalDateTime.now())
                .customerInfo(CustomerInfo.builder()
                        .customerId(1L)
                        .customerPhone("010-1234-1234")
                        .peopleCount(1)
                        .build())
                .waitingStatus(WaitingStatus.WAITING)
                .visitedTime(null)
                .build();
        waitingRedisTemplate.opsForHash().put("store:1", 1L, waiting);

        // when
        LocalTime start = LocalTime.now();
        List<Waiting> findResult = null;

        for (int i = 0; i < 100000; i++) {
            findResult = redisTemplate.opsForHash().values("store:1").stream()
                    .map(waitingModel -> {
                        try {
                            return objectMapper.readValue((String) waitingModel, Waiting.class);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toList();

        }
        LocalTime end = LocalTime.now();
        // then
        assertThat(findResult.size()).isEqualTo(1);

        System.out.println(" elapsed time = " + Duration.between(start, end).toMillis());
        // 11700 ~12000ms
    }

    @Test
    @DisplayName("Waiting Redis 데이터 여러건 조회 - objectMapper 사용 x")
    void findAllByWaitingRedisTemplate() {
        // given
        Waiting waiting = Waiting.builder()
                .waitingNumber(1L)
                .waitingOrder(1L)
                .storeId(1L)
                .createdDate(LocalDateTime.now())
                .customerInfo(CustomerInfo.builder()
                        .customerId(1L)
                        .customerPhone("010-1234-1234")
                        .peopleCount(1)
                        .build())
                .waitingStatus(WaitingStatus.WAITING)
                .visitedTime(null)
                .build();
        waitingRedisTemplate.opsForHash().put("store:1", 1L, waiting);

        // when
        LocalTime start = LocalTime.now();
        List<Waiting> findResult = null;

        for (int i = 0; i < 100000; i++) {
            findResult = waitingRedisTemplate.opsForHash().values("store:1").stream()
                    .map(waitingModel -> (Waiting) waitingModel)
                    .toList();
        }
        LocalTime end = LocalTime.now();
        // then
        assertThat(findResult.size()).isEqualTo(1);

        System.out.println(" elapsed time = " + Duration.between(start, end).toMillis());
        // 11700 ~12000ms
    }

    @Test
    @DisplayName("Waiting Redis 데이터 여러건 조회 - 캐스팅 사용 x")
    void findAllByWaitingRedisTemplateAndNotCasting() {
        // given
        Waiting waiting = Waiting.builder()
                .waitingNumber(1L)
                .waitingOrder(1L)
                .storeId(1L)
                .createdDate(LocalDateTime.now())
                .customerInfo(CustomerInfo.builder()
                        .customerId(1L)
                        .customerPhone("010-1234-1234")
                        .peopleCount(1)
                        .build())
                .waitingStatus(WaitingStatus.WAITING)
                .visitedTime(null)
                .build();
        waitingRedisTemplate.opsForHash().put("store:1", 1L, waiting);

        // when
        LocalTime start = LocalTime.now();
        List<Waiting> findResult = null;
        HashOperations<String, Long, Waiting> hashOperations = waitingRedisTemplate.opsForHash();
        for (int i = 0; i < 100000; i++) {
            findResult = hashOperations.values("store:1");
        }
        LocalTime end = LocalTime.now();
        // then
        assertThat(findResult.size()).isEqualTo(1);

        System.out.println(" elapsed time = " + Duration.between(start, end).toMillis());
        // 11400 ~11600 ms
    }

    @Test
    @DisplayName("WaitingNumber 생성 & 조회 - ObjectMapper 사용 o")
    void findWaitingNumberByRedisTemplate() {
        // given
        String key = "customer:" + 1;
        String field = "1";
        // when

        // 생성
        String currentValue = (String) redisTemplate.opsForHash().get(key, field);

        List<Long> waitingNumbers = null;
        try {
            if (currentValue == null) {
                waitingNumbers = new ArrayList<>();
            } else {
                waitingNumbers = objectMapper.readValue(currentValue, new TypeReference<List<Long>>() {
                });
            }
            waitingNumbers.add(1L);

            // JSON 배열로 직렬화하여 Redis에 저장
            redisTemplate.opsForHash().put(key, field, objectMapper.writeValueAsString(waitingNumbers));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 조회
        currentValue = (String) redisTemplate.opsForHash().get(key, field);
        try {
            if (currentValue == null) {
                waitingNumbers = new ArrayList<>();
            } else {
                waitingNumbers = objectMapper.readValue(currentValue, new TypeReference<List<Long>>() {
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // then
        assertThat(waitingNumbers.size()).isEqualTo(1);
        assertThat(waitingNumbers.get(0)).isEqualTo(1);
    }

    @Test
    @DisplayName("WaitingNumber 생성 & 조회 - ObjectMapper 사용 x")
    void findWaitingNumberByWaitingNumbersRedisTemplate() {
        // given
        String key = "customer:" + 1;
        Long field = 1L;
        // when

        // 생성
        WaitingNumbers currentValue = (WaitingNumbers) waitingNumbersRedisTemplate.opsForHash().get(key, field);

        if (currentValue == null) {
            currentValue = new WaitingNumbers();
        }

        currentValue.getWaitingNumbers().add(1L);

        // JSON 배열로 직렬화하여 Redis에 저장
        waitingNumbersRedisTemplate.opsForHash().put(key, field, currentValue);

        // 조회
        currentValue = (WaitingNumbers) waitingNumbersRedisTemplate.opsForHash().get(key, field);
        if (currentValue == null) {
            currentValue = new WaitingNumbers();
        }

        // then
        assertThat(currentValue.getWaitingNumbers().size()).isEqualTo(1);
        assertThat(currentValue.getWaitingNumbers().get(0)).isEqualTo(1);
    }

    @Test
    @DisplayName("waitingNumbersRedisTemplate 사용 속도 체크")
    void getAllWaitingNumbersByCustomerAndUsingRedisTemplate() {
        WaitingNumbers waitingNumbers = new WaitingNumbers();
        String key = "customer:" + 1L;
        for (long i = 0; i < 100; i++) {
            waitingNumbers.getWaitingNumbers().add(i);
        }

        for (int i = 0; i < 100000; i++) {
            waitingNumbersRedisTemplate.opsForHash().put(key, i, waitingNumbers);
        }

        LocalTime start = LocalTime.now();
        Map<Long, WaitingNumbers> findResult = null;
        for (int i = 0; i < 10; i++) {
            Map<Object, Object> rawEntries = waitingNumbersRedisTemplate.opsForHash().entries(key);

            findResult = rawEntries.entrySet().stream()
                    .collect(Collectors.toMap(
                            entry -> (Long) entry.getKey(),
                            entry -> (WaitingNumbers) entry.getValue()
                    ));
        }

        LocalTime end = LocalTime.now();
        assertThat(findResult.size()).isEqualTo(100000);
        System.out.println(" elapsed time = " + Duration.between(start, end).toMillis());

        // 4500 ~ 4700 ms
    }

    @Test
    @DisplayName("HashOperations 사용 속도 체크")
    void getAllWaitingNumbersByCustomerAndUsingHashOperations() {
        WaitingNumbers waitingNumbers = new WaitingNumbers();
        String key = "customer:" + 1L;
        for (long i = 0; i < 100; i++) {
            waitingNumbers.getWaitingNumbers().add(i);
        }

        for (int i = 0; i < 100000; i++) {
            waitingNumbersRedisTemplate.opsForHash().put(key, i, waitingNumbers);
        }
        HashOperations<String, Long, WaitingNumbers> hashOperations = waitingNumbersRedisTemplate.opsForHash();

        LocalTime start = LocalTime.now();
        for (int i = 0; i < 10; i++) {
            Map<Long, WaitingNumbers> rawEntries = hashOperations.entries(key);
        }

        LocalTime end = LocalTime.now();
        System.out.println(" elapsed time = " + Duration.between(start, end).toMillis());

        // 4400 ~ 4600 ms
    }
}
