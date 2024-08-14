package com.matdang.seatdang.waiting.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CustomerInfo {
    private Long customerId;
    private String customerPhone;
}
