package com.matdang.seatdang.customer.search.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MapService {
    @Value("${ncp.api.accessId}")
    private String accessId;
    @Value("${ncp.api.secretKey}")
    private String secretKey;
}
