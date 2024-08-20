package com.matdang.seatdang.waiting.controller.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WaitingRequest {
    private Long storeId;
    private Integer peopleCount;
}
