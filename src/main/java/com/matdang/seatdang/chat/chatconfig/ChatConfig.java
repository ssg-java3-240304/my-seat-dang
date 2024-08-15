package com.matdang.seatdang.chat.chatconfig;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class ChatConfig {
    @Value("${hostUrl}")
    private String serverUrl;
}
