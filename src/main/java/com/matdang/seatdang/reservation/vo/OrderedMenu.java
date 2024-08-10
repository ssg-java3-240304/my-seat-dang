package com.matdang.seatdang.reservation.vo;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class OrderedMenu {
    private String menuName;
    private int menuPrice;
    private String imageUrl;
    private CustomMenuOpt customMenuOpt;
}
