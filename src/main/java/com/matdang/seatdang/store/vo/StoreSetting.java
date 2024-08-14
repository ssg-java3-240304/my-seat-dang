package com.matdang.seatdang.store.vo;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class StoreSetting {
    private LocalTime reservationOpenTime;
    private LocalTime reservationCloseTime;
    @Enumerated(EnumType.STRING)
    private ReservationOnOff onOff;
    private LocalTime waitingOpenTime;
    private LocalTime waitingCloseTime;
    private LocalTime expectedWaitingTime;
    // waitingOnOff 필요
}
