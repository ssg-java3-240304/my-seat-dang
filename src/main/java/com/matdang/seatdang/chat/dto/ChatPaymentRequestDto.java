package com.matdang.seatdang.chat.dto;

import lombok.Data;

@Data
public class ChatPaymentRequestDto {
    private String itemName;
    private Integer quantity;
    private Integer totalAmount;
}
