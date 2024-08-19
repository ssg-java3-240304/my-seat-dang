package com.matdang.seatdang.waiting.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class WaitingStorage {

    @Id
    @GeneratedValue
    private Long id;

    private Long waitingNumber;
    private Long waitingOrder;
    private Long storeId;

    @Embedded
    private CustomerInfo customerInfo;

    private LocalDateTime createdDate;

    @Enumerated(EnumType.STRING)
    private WaitingStatus waitingStatus;
    private LocalDateTime visitedTime;

    public WaitingStorage(Long waitingNumber, Long waitingOrder, Long storeId, Long customerId
            , String customerPhone, Long peopleCount, LocalDateTime createdDate, WaitingStatus waitingStatus,
                          LocalDateTime visitedTime) {
        this.waitingNumber = waitingNumber;
        this.waitingOrder = waitingOrder;
        this.storeId = storeId;
        this.customerInfo = new CustomerInfo(customerId, customerPhone, peopleCount);
        this.createdDate = createdDate;
        this.waitingStatus = waitingStatus;
        this.visitedTime = visitedTime;
    }
}
