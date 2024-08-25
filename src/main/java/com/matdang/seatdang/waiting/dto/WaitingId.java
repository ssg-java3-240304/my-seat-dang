package com.matdang.seatdang.waiting.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WaitingId {
    private Long storeId;
    private Long waitingNumber;
}
