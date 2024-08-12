package com.matdang.seatdang.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NcpMapConfig {
    @Value("${ncp.api.accessId}")
    private String ncpApiAccessId;
    @Value("${ncp.api.secretKey}")
    private String ncpApiSecretKey;


}
