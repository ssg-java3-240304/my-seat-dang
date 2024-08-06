package com.matdang.seatdang.payment.dto;

import lombok.Data;

@Data
//@AllArgsConstructor
//@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ReadyResponse {
//    private String tid;
//    private Boolean tmsResult;
//    private String createdAt;
//    private String nextRedirectRcUrl;
//    private String nextRedirectMobileUrl;
//    private String nextRedirectAppUrl;
//    private String androidAppScheme;
//    private String iosAppScheme;
private String tid;
    private Boolean tms_result;
    private String created_at;
    private String next_redirect_pc_url;
    private String next_redirect_mobile_url;
    private String next_redirect_app_url;
    private String android_app_scheme;
    private String ios_app_scheme;
}
