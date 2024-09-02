package com.matdang.seatdang.waiting.redis;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
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
