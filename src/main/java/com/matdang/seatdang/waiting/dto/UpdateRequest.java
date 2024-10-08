package com.matdang.seatdang.waiting.dto;

import lombok.Data;

@Data
public class UpdateRequest {
    private Long waitingNumber;
    private Long storeId;
    private Long waitingOrder;
    private Integer changeStatus;
    private Integer status = 0;
}
