package com.matdang.seatdang.reservation.vo;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class CustomMenuOpt {
    private String sheet;
    private String size;
    private String cream;
    private String lettering;
}
