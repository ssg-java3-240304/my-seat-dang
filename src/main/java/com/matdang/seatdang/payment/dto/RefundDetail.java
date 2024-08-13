package com.matdang.seatdang.payment.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RefundDetail {
    private String tid;
    private Long partnerOrderId;
    private Long partnerUserId;
    private Long shopId;
    private Integer cancelAmount;
    private Integer cancelTaxFreeAmount;
}
