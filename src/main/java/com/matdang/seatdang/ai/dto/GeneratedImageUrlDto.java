package com.matdang.seatdang.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeneratedImageUrlDto {
    private String generatedUrl;
    private LocalDateTime createdAt;
    private String inputText;
}
