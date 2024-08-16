package com.matdang.seatdang.store.vo;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class StoreSetting {
    private LocalTime reservationOpenTime;
    private LocalTime reservationCloseTime;
    private ReservationOnOff reservationOnOff;
    private LocalTime waitingOpenTime;
    private LocalTime waitingCloseTime;
    private ReservationOnOff waitingOnOff;
    private LocalTime expectedWaitingTime;
}
