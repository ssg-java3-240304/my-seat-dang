package com.matdang.seatdang.reservation.vo;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class CustomerInfo {
    private Long CustomerId;
    private String CustomerName;
    private String CustomerPhone;
}
