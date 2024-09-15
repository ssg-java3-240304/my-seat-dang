package com.matdang.seatdang.waiting.controller.dto;

import com.matdang.seatdang.store.entity.Store;
import com.matdang.seatdang.waiting.redis.Waiting;
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
public class CanceledWaitingResponse {
    private Long waitingNumber;
    private WaitingStatus waitingStatus;
    private Integer peopleCount;
    private String storeName;

    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime createdDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime canceledTime;

    public static CanceledWaitingResponse create(Object waiting, Store store) {
        CanceledWaitingResponse canceledWaitingResponse = new CanceledWaitingResponse();
        if (waiting instanceof Waiting) {
            canceledWaitingResponse.waitingNumber = ((Waiting) waiting).getWaitingNumber();
            canceledWaitingResponse.waitingStatus = ((Waiting) waiting).getWaitingStatus();
            canceledWaitingResponse.createdDate = ((Waiting) waiting).getCreatedDate();
            canceledWaitingResponse.peopleCount = ((Waiting) waiting).getCustomerInfo().getPeopleCount();
            canceledWaitingResponse.canceledTime = ((Waiting) waiting).getCanceledTime();
            canceledWaitingResponse.storeName = store.getStoreName();
        } else if (waiting instanceof WaitingStorage) {
            canceledWaitingResponse.waitingNumber = ((WaitingStorage) waiting).getWaitingNumber();
            canceledWaitingResponse.waitingStatus = ((WaitingStorage) waiting).getWaitingStatus();
            canceledWaitingResponse.createdDate = ((WaitingStorage) waiting).getCreatedDate();
            canceledWaitingResponse.peopleCount = ((WaitingStorage) waiting).getCustomerInfo().getPeopleCount();
            canceledWaitingResponse.canceledTime = ((WaitingStorage) waiting).getCanceledTime();
            canceledWaitingResponse.storeName = store.getStoreName();
        }

        return canceledWaitingResponse;
    }
}
