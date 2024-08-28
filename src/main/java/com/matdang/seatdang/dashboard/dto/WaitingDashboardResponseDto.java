package com.matdang.seatdang.dashboard.dto;

import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Data
@NoArgsConstructor
public class WaitingDashboardResponseDto {
    // 최근 웨이팅 건수 (controller와 맞춤)
    private Long cnt;
    private LocalDate visitedTime;

    public WaitingDashboardResponseDto(Long cnt, String visitedTime) {
        this.cnt = cnt;
        this.visitedTime = LocalDate.parse(visitedTime);
    }

    //    public WaitingDashboardResponseDto(Long cnt) {
//        this.cnt = cnt;
//    }
}
