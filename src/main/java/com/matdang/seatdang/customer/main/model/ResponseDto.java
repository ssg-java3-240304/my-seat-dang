package com.matdang.seatdang.customer.main.model;

import lombok.Data;

@Data
public class ResponseDto {
    private Long reservationId;
    private String customerName;
    private Long customerId;
    private Long storeId;
    private String storeName;
}
