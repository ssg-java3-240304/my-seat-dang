package com.matdang.seatdang.payment.dto;


import lombok.*;

@Data
@Builder
@AllArgsConstructor
public class PayDetail {
    private Long partnerOrderId; // reservationId
    private Long partnerUserId; // customerId
    private Long shopId; // storeId
    private String itemName;
    private Integer quantity;
    private Integer totalAmount;
    private Integer taxFreeAmount; // 상품 비과세 금액 지정값 :0
}
