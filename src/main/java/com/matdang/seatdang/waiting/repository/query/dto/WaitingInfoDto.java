package com.matdang.seatdang.waiting.repository.query.dto;

import com.matdang.seatdang.waiting.entity.WaitingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WaitingInfoDto {
    private Long id;
    private String storeName;
    private Long waitingNumber;

    private Integer peopleCount;
    private WaitingStatus waitingStatus;

}
