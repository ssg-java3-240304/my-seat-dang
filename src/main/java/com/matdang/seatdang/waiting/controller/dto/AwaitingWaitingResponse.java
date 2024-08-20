package com.matdang.seatdang.waiting.controller.dto;

import com.matdang.seatdang.store.entity.Store;
import com.matdang.seatdang.waiting.entity.Waiting;
import com.matdang.seatdang.waiting.entity.WaitingStatus;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AwaitingWaitingResponse {
    private Long waitingId;
    private Long waitingNumber;
    private WaitingStatus waitingStatus;
    private LocalDateTime createdDate;
    private Integer peopleCount;
    private String storeName;

    public static AwaitingWaitingResponse create(Waiting waiting, Store store) {
        AwaitingWaitingResponse awaitingWaitingResponse = new AwaitingWaitingResponse();
        awaitingWaitingResponse.waitingId = waiting.getId();
        awaitingWaitingResponse.waitingNumber = waiting.getWaitingNumber();
        awaitingWaitingResponse.waitingStatus = waiting.getWaitingStatus();
        awaitingWaitingResponse.createdDate = waiting.getCreatedDate();
        awaitingWaitingResponse.peopleCount = waiting.getCustomerInfo().getPeopleCount();
        awaitingWaitingResponse.storeName = store.getStoreName();

        return awaitingWaitingResponse;
    }
}
