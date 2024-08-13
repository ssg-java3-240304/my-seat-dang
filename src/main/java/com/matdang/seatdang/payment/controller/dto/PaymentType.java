package com.matdang.seatdang.payment.controller.dto;

import lombok.Getter;

@Getter
public enum PaymentType {

    CARD("카드"),
    MONEY("현금");

    private final String type;

    PaymentType(String type) {
        this.type = type;
    }
}
