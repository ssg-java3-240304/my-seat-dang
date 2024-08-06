package com.matdang.seatdang.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.matdang.seatdang.chat.repository")
public class ChatConfig {

    @Bean
    public MongoClient mongoClient() {
        // MongoDB 클라이언트를 생성하는 부분
        return MongoClients.create("mongodb://localhost:27017");
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
        // MongoTemplate을 정의하는 부분, MongoDB의 데이터베이스 이름을 명시
        return new MongoTemplate(mongoClient, "chatdb");
    }
}

