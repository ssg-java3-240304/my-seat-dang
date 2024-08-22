package com.matdang.seatdang.reservation.dto;

import com.matdang.seatdang.member.entity.MemberRole;
import com.matdang.seatdang.reservation.vo.ReservationCancellationRecord;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationCancelRequestDto {
    private Long reservationId;
    private String reason;
    private MemberRole cancelledBy;
    private LocalDateTime cancelledAt;

    public ReservationCancellationRecord convertToReservationCancellationRecord() {
        return ReservationCancellationRecord.builder()
                .reason(this.reason)
                .cancelledBy(this.cancelledBy)
                .cancelledAt(this.cancelledAt)
                .build();
    }
}
