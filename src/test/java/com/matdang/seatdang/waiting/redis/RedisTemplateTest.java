package com.matdang.seatdang.waiting.redis;

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

@SpringBootTest
class RedisTemplateTest {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private RedisTemplate<String, Waiting> waitingRedisTemplate;

    /**

     */
    @Test
    @DisplayName("Waiting Redis 데이터 생성")
    void create() {
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
//        redisTemplate.opsForHash().put("store:1", "1",waiting );
        LocalTime start = LocalTime.now();
        waitingRedisTemplate.opsForHash().put("store:1", 1L, waiting);
        LocalTime end = LocalTime.now();

        System.out.println(" elapsed time = "+ Duration.between(start, end).toMillis());
    }

    // 328
}
