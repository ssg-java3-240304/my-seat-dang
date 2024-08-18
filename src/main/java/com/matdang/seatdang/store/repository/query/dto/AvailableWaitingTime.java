package com.matdang.seatdang.store.repository.query.dto;

import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

@Data
// 삭제예정
@Setter(AccessLevel.PRIVATE)
@AllArgsConstructor
public class AvailableWaitingTime {
    private LocalTime waitingOpenTime;
    private LocalTime waitingCloseTime;
}
