package com.matdang.seatdang.waiting.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Waiting {
    @Id @GeneratedValue
    private Long id;

    private Long waitingNumber;
    private Long waitingOrder;
    private Long storeId;

    @Embedded
    private CustomerInfo customerInfo;

    @Enumerated(EnumType.STRING)
    private WaitingStatus waitingStatus;

    // TODO : @CreatedDate 사용
    private LocalDateTime createdAt;
    private LocalDateTime visitedTime;
}
