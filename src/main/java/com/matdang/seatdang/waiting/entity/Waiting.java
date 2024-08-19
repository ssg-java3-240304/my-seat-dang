package com.matdang.seatdang.waiting.entity;

import com.matdang.seatdang.payment.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class Waiting extends BaseEntity {
    @Id @GeneratedValue
    private Long id;

    private Long waitingNumber;
    private Long waitingOrder;
    private Long storeId;

    @Embedded
    private CustomerInfo customerInfo;

    @Enumerated(EnumType.STRING)
    private WaitingStatus waitingStatus;
    private LocalDateTime visitedTime;
}
