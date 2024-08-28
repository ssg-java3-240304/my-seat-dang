package com.matdang.seatdang.common.storeEnum;

public enum StoreType {
    GENERAL_WAITING("웨이팅"), // 웨이팅만 되는 일반빵
    GENERAL_RESERVATION("예약 전문"), //예약만 되는 일반빵
    CUSTOM("주문제작"); //예약만 되는 커스텀케이크

    private final String description;

    StoreType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}