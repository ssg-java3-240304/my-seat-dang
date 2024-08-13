package com.matdang.seatdang.payment.service.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RefundRequest {
    private String tid;
    private String cid;
    private Integer cancelAmount;
    private Integer cancelTaxFreeAmount;
}
