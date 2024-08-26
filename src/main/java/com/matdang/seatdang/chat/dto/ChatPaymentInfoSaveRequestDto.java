package com.matdang.seatdang.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatPaymentInfoSaveRequestDto {
    private String sender;
    private String roomNum;
    private String message;
    private Integer totalAmount;
    private String itemName;
}
