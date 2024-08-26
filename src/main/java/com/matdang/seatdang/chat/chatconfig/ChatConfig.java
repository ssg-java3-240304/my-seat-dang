package com.matdang.seatdang.chat.chatconfig;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class ChatConfig {
    @Value("${hostUrl}")
    private String serverUrl;
}
