package com.matdang.seatdang.waiting.repository.query.dto;

import com.matdang.seatdang.waiting.entity.WaitingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class WaitingInfoDto {
    private Long waitingNumber;
    private Long storeId;
    private String storeName;

    private Integer peopleCount;
    private WaitingStatus waitingStatus;
    private LocalDateTime createDate;

}
