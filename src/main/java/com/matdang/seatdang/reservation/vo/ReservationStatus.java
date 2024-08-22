package com.matdang.seatdang.reservation.vo;

public enum ReservationStatus {
    CREATING, DETAILING, AWAITING_PAYMENT, PAYMENT_COMPLETED, BEFORE_VISIT, VISIT_COMPLETED, CANCELED_BY_OWNER, CANCELED_BY_CUSTOMER;

    public boolean canTransitionTo(ReservationStatus newStatus) {
        // 현재 상태의 순서가 전환하려는 상태의 순서보다 작아야만 전환 가능
        if(!(newStatus == CANCELED_BY_CUSTOMER || newStatus == CANCELED_BY_OWNER))
            return this.ordinal() < newStatus.ordinal();
        return this.ordinal() < ReservationStatus.VISIT_COMPLETED.ordinal();
    }
}