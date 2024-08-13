package com.matdang.seatdang.payment.dto;

import lombok.Data;

@Data
public class ReadyRedirect {
    private String pg_token; // kakao api에서 정의된 명칭
    private String partnerOrderId;
}
