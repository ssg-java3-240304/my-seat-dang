package com.matdang.seatdang.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.matdang.seatdang.waiting.redis.Waiting;
import com.matdang.seatdang.waiting.redis.WaitingNumbers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

//@EnableCaching
@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private String port;

    @Value("${spring.data.redis.password}")
    private String password;


    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(Integer.parseInt(port));
        if (password != null && !password.isEmpty()) {
            redisStandaloneConfiguration.setPassword(password);
        }
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration);
        return lettuceConnectionFactory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        // Redis에서 key를 String으로, value를 JSON 형태로 저장하도록 설정
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }

    @Bean
    public RedisTemplate<String, WaitingNumbers> waitingNumbersRedisTemplate() {
        RedisTemplate<String, WaitingNumbers> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        // Redis에서 key를 String으로, value를 JSON 형태로 저장하도록 설정
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new GenericToStringSerializer<>(Long.class));

        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(WaitingNumbers.class));
        return redisTemplate;
    }

    @Bean
    public RedisTemplate<String, Waiting> waitingRedisTemplate() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        RedisTemplate<String, Waiting> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        // Redis에서 key를 String으로, value를 JSON 형태로 저장하도록 설정
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new GenericToStringSerializer<>(Long.class));
//        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Waiting.class));
//        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Waiting.class));

        Jackson2JsonRedisSerializer<Waiting> serializer = new Jackson2JsonRedisSerializer<>(objectMapper,
                Waiting.class);
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashValueSerializer(serializer);

        return redisTemplate;
    }
}