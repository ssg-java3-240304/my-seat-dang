package com.matdang.seatdang.waiting.controller.dto;

import com.matdang.seatdang.store.entity.Store;
import com.matdang.seatdang.waiting.entity.Waiting;
import com.matdang.seatdang.waiting.entity.WaitingStatus;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VisitedWaitingResponse {
    private Long waitingNumber;
    private WaitingStatus waitingStatus;
    private Integer peopleCount;
    private String storeName;

    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime createdDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime visitedTime;

    public static VisitedWaitingResponse create(Waiting waiting, Store store) {
        VisitedWaitingResponse visitedWaitingResponse = new VisitedWaitingResponse();
        visitedWaitingResponse.waitingNumber = waiting.getWaitingNumber();
        visitedWaitingResponse.waitingStatus = waiting.getWaitingStatus();
        visitedWaitingResponse.createdDate = waiting.getCreatedDate();
        visitedWaitingResponse.peopleCount = waiting.getCustomerInfo().getPeopleCount();
        visitedWaitingResponse.visitedTime = waiting.getVisitedTime();
        visitedWaitingResponse.storeName = store.getStoreName();

        return visitedWaitingResponse;
    }
}
