package com.matdang.seatdang.store.entity;

import jakarta.persistence.Embeddable;

import java.time.LocalTime;
@Embeddable
public class StoreSetting {
    private LocalTime reservationOpenTime;
    private LocalTime reservationCloseTime;
    private ReservationOnOff onOff;
    private LocalTime waitingOpenTime;
    private LocalTime waitingCloseTime;
    private LocalTime expectedWaitingTime;
    // waitingOnOff 필요
}
