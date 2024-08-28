//package com.matdang.seatdang.ai.service;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.io.IOException;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@ActiveProfiles("test")
//@SpringBootTest
//class AIServiceTest {
//
//    @Autowired
//    private CakeDesignService aiService; // Spring이 관리하는 CakeDesignService 빈 주입
//
//
//    @BeforeEach
//    void setUp() {
//    }
//
//    @AfterEach
//    void tearDown() {
//    }
//
//    @Test
//    public void testGeneratePicturePerformance() throws IOException, InterruptedException {
//
//        // 성능 테스트 실행
//        aiService.testExecutionTimeWithAverage();
//    }
//}