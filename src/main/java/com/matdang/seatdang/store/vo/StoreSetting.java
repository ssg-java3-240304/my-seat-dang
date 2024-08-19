package com.matdang.seatdang.store.vo;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.EnumType;
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
    @Enumerated(EnumType.STRING)
    private Status reservationStatus;

    @Embedded
    private WaitingTime waitingTime;

    @Enumerated(EnumType.STRING)
    private WaitingStatus waitingStatus = WaitingStatus.UNAVAILABLE;

    private Integer waitingPeopleCount;
}
