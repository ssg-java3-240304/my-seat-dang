package com.matdang.seatdang.waiting.controller.dto;

import com.matdang.seatdang.store.entity.Store;
import com.matdang.seatdang.waiting.entity.Waiting;
import com.matdang.seatdang.waiting.entity.WaitingStatus;
import com.matdang.seatdang.waiting.entity.WaitingStorage;
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

    public static VisitedWaitingResponse create(Object waiting, Store store) {
        VisitedWaitingResponse visitedWaitingResponse = new VisitedWaitingResponse();
        if (waiting instanceof Waiting) {
            visitedWaitingResponse.waitingNumber = ((Waiting) waiting).getWaitingNumber();
            visitedWaitingResponse.waitingStatus = ((Waiting) waiting).getWaitingStatus();
            visitedWaitingResponse.createdDate = ((Waiting) waiting).getCreatedDate();
            visitedWaitingResponse.peopleCount = ((Waiting) waiting).getCustomerInfo().getPeopleCount();
            visitedWaitingResponse.visitedTime = ((Waiting) waiting).getVisitedTime();
            visitedWaitingResponse.storeName = store.getStoreName();
        } else if (waiting instanceof WaitingStorage) {
            visitedWaitingResponse.waitingNumber = ((WaitingStorage) waiting).getWaitingNumber();
            visitedWaitingResponse.waitingStatus = ((WaitingStorage) waiting).getWaitingStatus();
            visitedWaitingResponse.createdDate = ((WaitingStorage) waiting).getCreatedDate();
            visitedWaitingResponse.peopleCount = ((WaitingStorage) waiting).getCustomerInfo().getPeopleCount();
            visitedWaitingResponse.visitedTime = ((WaitingStorage) waiting).getVisitedTime();
            visitedWaitingResponse.storeName = store.getStoreName();
        }

        return visitedWaitingResponse;
    }

}
