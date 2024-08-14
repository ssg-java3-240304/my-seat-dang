package com.matdang.seatdang.payment.controller.dto;

import lombok.Getter;

@Getter
public enum RefundStatus {
    PART_CANCEL_PAYMENT("부분 취소"),
    CANCEL_PAYMENT("모두 취소");

    private final String status;

    RefundStatus(String status) {
        this.status = status;
    }

}
