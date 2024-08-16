package com.matdang.seatdang.menu.vo;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class CustomMenuOpt{
    private String sheet;
    private String size;
    private String cream;
    private String lettering;
}
