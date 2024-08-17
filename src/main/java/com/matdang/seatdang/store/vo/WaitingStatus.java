package com.matdang.seatdang.store.vo;

import lombok.Getter;

@Getter
public enum WaitingStatus {
    OPEN("웨이팅 접수"),
    CLOSE("웨이팅 마감"),
    UNAVAILABLE("웨이팅 이용불가");

    private final String waitingStatus;

    WaitingStatus(String waitingStatus) {
        this.waitingStatus = waitingStatus;
    }
}
