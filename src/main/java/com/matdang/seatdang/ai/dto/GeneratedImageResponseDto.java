package com.matdang.seatdang.ai.dto;

import com.matdang.seatdang.ai.entity.GeneratedImageUrl;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeneratedImageResponseDto {
    private String url;
    private String description;
    private LocalDateTime createdAt;

}
