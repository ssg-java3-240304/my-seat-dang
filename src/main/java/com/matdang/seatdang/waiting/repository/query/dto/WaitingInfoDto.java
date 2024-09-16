package com.matdang.seatdang.waiting.repository.query.dto;

import com.matdang.seatdang.waiting.entity.WaitingStatus;
import com.matdang.seatdang.waiting.redis.Waiting;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WaitingInfoDto {
    private Long waitingNumber;
    private Long storeId;
    private String storeName;

    private Integer peopleCount;
    private WaitingStatus waitingStatus;
    private LocalDateTime createDate;

    public WaitingInfoDto(Long waitingNumber, Long storeId, String storeName, Integer peopleCount, WaitingStatus waitingStatus) {
        this.waitingNumber = waitingNumber;
        this.storeId = storeId;
        this.storeName = storeName;
        this.peopleCount = peopleCount;
        this.waitingStatus = waitingStatus;
    }

    public WaitingInfoDto(Waiting waiting, String storeName) {
        this.waitingNumber = waiting.getWaitingNumber();
        this.storeId = waiting.getStoreId();
        this.storeName = storeName;
        this.peopleCount = waiting.getCustomerInfo().getPeopleCount();
        this.waitingStatus = waiting.getWaitingStatus();
        this.createDate = waiting.getCreatedDate();
    }
}
