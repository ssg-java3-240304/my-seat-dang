package com.matdang.seatdang.store.vo;

import jakarta.persistence.Embeddable;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Data
@Builder
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WaitingTime {
    private LocalTime waitingOpenTime;
    private LocalTime waitingCloseTime;
    private LocalTime expectedWaitingTime;
}
