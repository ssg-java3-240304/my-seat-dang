package com.matdang.seatdang.waiting.controller.dto;

import com.matdang.seatdang.store.entity.Store;
import lombok.*;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReadyWaitingResponse {
    private Long storeId;
    private String storeName;
    private Integer waitingPeopleCount;

    public static ReadyWaitingResponse create(Store store) {
        ReadyWaitingResponse readyWaitingResponse = new ReadyWaitingResponse();
        readyWaitingResponse.storeId = store.getStoreId();
        readyWaitingResponse.storeName = store.getStoreName();
        readyWaitingResponse.waitingPeopleCount = store.getStoreSetting().getWaitingPeopleCount();

        return readyWaitingResponse;
    }
}
