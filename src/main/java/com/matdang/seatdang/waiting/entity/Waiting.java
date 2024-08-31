package com.matdang.seatdang.waiting.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
//@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Waiting  {
    @Id @GeneratedValue
    private Long id;

    private Long waitingNumber;
    private Long waitingOrder;
    private Long storeId;

    @Embedded
    private CustomerInfo customerInfo;

    @Enumerated(EnumType.STRING)
    private WaitingStatus waitingStatus;

    private LocalDateTime createdDate;
    private LocalDateTime visitedTime;
    private LocalDateTime canceledTime;

    public WaitingStorage convertToWaitingStorage() {
        return WaitingStorage.builder()
                .waitingNumber(this.waitingNumber)
                .waitingOrder(this.waitingOrder)
                .storeId(this.storeId)
                .customerInfo(this.customerInfo) // Assuming CustomerInfo is directly usable
                .createdDate(this.createdDate)
                .waitingStatus(this.waitingStatus)
                .visitedTime(this.visitedTime)
                .canceledTime(this.canceledTime)
                .build();
    }
}
