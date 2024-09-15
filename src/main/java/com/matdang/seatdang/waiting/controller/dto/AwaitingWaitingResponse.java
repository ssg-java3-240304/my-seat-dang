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
public class AwaitingWaitingResponse {
    private Long waitingId;
    private Long waitingNumber;
    private Long waitingOrder;
    private WaitingStatus waitingStatus;
    private Integer peopleCount;
    private Long storeId;
    private String storeName;

    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime createdDate;

    public static AwaitingWaitingResponse create(Object waiting, Store store) {
        AwaitingWaitingResponse awaitingWaitingResponse = new AwaitingWaitingResponse();
        if (waiting instanceof Waiting) {
            awaitingWaitingResponse.waitingNumber = ((Waiting) waiting).getWaitingNumber();
            awaitingWaitingResponse.waitingOrder = ((Waiting) waiting).getWaitingOrder();
            awaitingWaitingResponse.waitingStatus = ((Waiting) waiting).getWaitingStatus();
            awaitingWaitingResponse.createdDate = ((Waiting) waiting).getCreatedDate();
            awaitingWaitingResponse.peopleCount = ((Waiting) waiting).getCustomerInfo().getPeopleCount();
            awaitingWaitingResponse.storeName = store.getStoreName();
            awaitingWaitingResponse.storeId = store.getStoreId();
        } else if (waiting instanceof WaitingStorage) {
            awaitingWaitingResponse.waitingId = ((WaitingStorage) waiting).getId();
            awaitingWaitingResponse.waitingNumber = ((WaitingStorage) waiting).getWaitingNumber();
            awaitingWaitingResponse.waitingOrder = ((WaitingStorage) waiting).getWaitingOrder();
            awaitingWaitingResponse.waitingStatus = ((WaitingStorage) waiting).getWaitingStatus();
            awaitingWaitingResponse.createdDate = ((WaitingStorage) waiting).getCreatedDate();
            awaitingWaitingResponse.peopleCount = ((WaitingStorage) waiting).getCustomerInfo().getPeopleCount();
            awaitingWaitingResponse.storeName = store.getStoreName();
            awaitingWaitingResponse.storeId = store.getStoreId();
        }

        return awaitingWaitingResponse;
    }
}
