package com.matdang.seatdang.waiting.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.matdang.seatdang.waiting.entity.CustomerInfo;
import com.matdang.seatdang.waiting.entity.WaitingStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RedisTemplateTest {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private RedisTemplate<String, Waiting> waitingRedisTemplate;
    @Autowired
    private ObjectMapper objectMapper;

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

        System.out.println(" elapsed time = "+ Duration.between(start, end).toMillis());
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

        System.out.println(" elapsed time = "+ Duration.between(start, end).toMillis());
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
        // 400 ~ 500ms
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
                findResult = objectMapper.readValue((String) redisTemplate.opsForHash().get("store:1", "1"), Waiting.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        LocalTime end = LocalTime.now();
        // then
        assertThat(findResult.getWaitingStatus()).isEqualTo(waiting.getWaitingStatus());

        System.out.println(" elapsed time = " + Duration.between(start, end).toMillis());
        // 400 ~500ms
    }
}
