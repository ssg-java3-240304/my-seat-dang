package com.matdang.seatdang.reservation.vo;

public enum ReservationStatus {
    PENDING("예약준비"),
    AWAITING_PAYMENT("결제 대기"),
    PAYMENT_COMPLETED("결제 완료"),
    BEFORE_VISIT("방문 전"),
    VISIT_COMPLETED("방문 완료"),
    CANCELED("취소됨");

    private final String description;

    ReservationStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean canTransitionTo(ReservationStatus newStatus) {
        // 현재 상태의 순서가 전환하려는 상태의 순서보다 작아야만 전환 가능
        if(newStatus != CANCELED)
            return this.ordinal() < newStatus.ordinal();
        return this.ordinal() < ReservationStatus.VISIT_COMPLETED.ordinal();
    }
}