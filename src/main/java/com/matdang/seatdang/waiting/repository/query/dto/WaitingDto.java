package com.matdang.seatdang.waiting.repository.query.dto;

import com.matdang.seatdang.waiting.entity.WaitingStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WaitingDto {
    private Long id;
    private Long waitingNumber;
    private Long waitingOrder;

    private String customerPhone;
    private Long peopleCount;
    private WaitingStatus waitingStatus;

    private LocalDateTime createdAt;
    private LocalDateTime visitedTime;
}
