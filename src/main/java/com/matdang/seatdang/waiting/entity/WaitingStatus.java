package com.matdang.seatdang.waiting.entity;

import lombok.Getter;

@Getter
public enum WaitingStatus {
    WAITING(0, "대기"),
    VISITED(1, "입장"),
    SHOP_CANCELED(2, "상점 취소"),
    NO_SHOW(3, "노쇼"),
    CUSTOMER_CANCELED(4, "고객 취소");


    private final int index;
    private final String status;

    WaitingStatus(int index, String status) {
        this.index = index;
        this.status = status;
    }

    public static WaitingStatus findWaiting(int index) {
        return WaitingStatus.values()[index];
    }
}
