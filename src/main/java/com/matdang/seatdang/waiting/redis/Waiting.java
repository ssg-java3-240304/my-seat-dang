package com.matdang.seatdang.waiting.redis;

import com.matdang.seatdang.waiting.entity.CustomerInfo;
import com.matdang.seatdang.waiting.entity.WaitingStatus;
import com.matdang.seatdang.waiting.entity.WaitingStorage;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class Waiting  {
    private Long waitingNumber;
    private Long waitingOrder;
    private Long storeId;

    private CustomerInfo customerInfo;
    private WaitingStatus waitingStatus;

    private LocalDateTime createdDate;
    private LocalDateTime visitedTime;
    private LocalDateTime canceledTime;

    public WaitingStorage convertToWaitingStorage() {
        return WaitingStorage.builder()
                .waitingNumber(this.waitingNumber)
                .waitingOrder(this.waitingOrder)
                .storeId(this.storeId)
                .customerInfo(this.customerInfo)
                .createdDate(this.createdDate)
                .waitingStatus(this.waitingStatus)
                .visitedTime(this.visitedTime)
                .canceledTime(this.canceledTime)
                .build();
    }
}
