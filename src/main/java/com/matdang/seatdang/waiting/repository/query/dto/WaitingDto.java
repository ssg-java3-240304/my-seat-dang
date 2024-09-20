package com.matdang.seatdang.waiting.repository.query.dto;

import com.matdang.seatdang.waiting.redis.Waiting;
import com.matdang.seatdang.waiting.entity.WaitingStatus;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WaitingDto {
    private Long waitingNumber;
    private Long waitingOrder;

    private String customerPhone;
    private Integer peopleCount;
    private WaitingStatus waitingStatus;

    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime createdDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime visitedTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime canceledTime;


    public static WaitingDto create(Waiting waiting) {
        WaitingDto waitingDto = new WaitingDto();
        waitingDto.waitingNumber = waiting.getWaitingNumber();
        waitingDto.waitingOrder = waiting.getWaitingOrder();
        waitingDto.customerPhone = waiting.getCustomerInfo().getCustomerPhone();
        waitingDto.peopleCount = waiting.getCustomerInfo().getPeopleCount();
        waitingDto.waitingStatus = waiting.getWaitingStatus();
        waitingDto.createdDate = waiting.getCreatedDate();
        waitingDto.visitedTime = waiting.getVisitedTime();
        waitingDto.canceledTime = waiting.getCanceledTime();

        return waitingDto;
    }
}
