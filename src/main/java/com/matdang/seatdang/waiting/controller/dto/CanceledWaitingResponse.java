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
public class CanceledWaitingResponse {
    private Long waitingNumber;
    private WaitingStatus waitingStatus;
    private Integer peopleCount;
    private String storeName;

    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime createdDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime canceledTime;

    public static CanceledWaitingResponse create(Waiting waiting, Store store) {
        CanceledWaitingResponse canceledWaitingResponse = new CanceledWaitingResponse();
        canceledWaitingResponse.waitingNumber = waiting.getWaitingNumber();
        canceledWaitingResponse.waitingStatus = waiting.getWaitingStatus();
        canceledWaitingResponse.createdDate = waiting.getCreatedDate();
        canceledWaitingResponse.peopleCount = waiting.getCustomerInfo().getPeopleCount();
        canceledWaitingResponse.canceledTime = waiting.getCanceledTime();
        canceledWaitingResponse.storeName = store.getStoreName();

        return canceledWaitingResponse;
    }
}
