package com.matdang.seatdang.waiting.repository.query.dto;

import com.matdang.seatdang.waiting.entity.WaitingStatus;
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
}
