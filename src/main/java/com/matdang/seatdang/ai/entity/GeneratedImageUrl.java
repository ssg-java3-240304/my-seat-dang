package com.matdang.seatdang.ai.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tbl_generated_image_url")
public class GeneratedImageUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "url_id")
    private Long urlId;

    @Column(name = "customer_id", nullable = false)
    private Long customerId; // Customer의 ID를 직접 저장

//    @Column(nullable = false)
    @Column(nullable = false, columnDefinition = "TEXT")
    private String generatedUrl; // ncp에 저장된 url

    @Column(nullable = false)
    private LocalDateTime createdAt; // 생성날짜

    @Column(nullable = false)
    private String inputText; // 사용자가 입력한 값


}