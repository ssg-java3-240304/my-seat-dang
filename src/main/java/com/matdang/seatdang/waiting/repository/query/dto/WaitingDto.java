package com.matdang.seatdang.waiting.repository.query.dto;

import com.matdang.seatdang.waiting.entity.WaitingStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@AllArgsConstructor
public class WaitingDto {
    private Long id;
    private Long waitingNumber;
    private Long waitingOrder;

    private String customerPhone;
    private Integer peopleCount;
    private WaitingStatus waitingStatus;

    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime createdDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime visitedTime;
}
