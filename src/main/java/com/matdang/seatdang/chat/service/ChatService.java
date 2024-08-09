package com.matdang.seatdang.chat.service;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ChatService {
    @Value("${hostUrl}")
    private String serverUrl;
}
