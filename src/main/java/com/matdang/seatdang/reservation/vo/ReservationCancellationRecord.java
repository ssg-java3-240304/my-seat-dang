package com.matdang.seatdang.reservation.vo;

import com.matdang.seatdang.member.entity.MemberRole;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Setter(AccessLevel.PRIVATE)
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReservationCancellationRecord {
    private String reason;
    @Enumerated(EnumType.STRING)
    private MemberRole cancelledBy;
    private LocalDateTime cancelledAt;
}
