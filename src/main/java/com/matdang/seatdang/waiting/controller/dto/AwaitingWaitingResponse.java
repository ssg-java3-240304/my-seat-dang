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
public class AwaitingWaitingResponse {
    private Long waitingId;
    private Long waitingNumber;
    private WaitingStatus waitingStatus;
    private Integer peopleCount;
    private String storeName;

    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime createdDate;

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
