package com.matdang.seatdang.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.matdang.seatdang.chat.repository")
public class ChatConfig {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Bean
    public MongoClient mongoClient() {
        // MongoDB 클라이언트를 생성하는 부분
        return MongoClients.create("mongodb://ssg-java3.iptime.org:27017");
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
        // MongoTemplate을 정의하는 부분, MongoDB의 데이터베이스 이름을 명시
        return new MongoTemplate(mongoClient, "chatdb");
    }

    @Bean
    public void initializeCollections() {
        ensureCappedCollectionExists();
    }

    public void ensureCappedCollectionExists() {
        if (!mongoTemplate.collectionExists("chat")) {
            mongoTemplate.createCollection("chat", CollectionOptions.empty()
                    .capped()
                    .maxDocuments(5000)
                    .size(1000000));
        }
    }
}

