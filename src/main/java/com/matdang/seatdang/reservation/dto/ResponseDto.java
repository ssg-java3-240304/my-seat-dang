package com.matdang.seatdang.reservation.dto;

import lombok.Data;

@Data
public class ResponseDto {
    private Long reservationId;
    private String customerName;
    private Long customerId;
    private Long storeId;
    private String storeName;
    private Long storeOwnerId;
    private String storeOwnerName;
}
