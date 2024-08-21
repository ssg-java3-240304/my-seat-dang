package com.matdang.seatdang.reservation.vo;

import com.matdang.seatdang.menu.vo.MenuType;
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
    private MenuType menuType;
    private CustomMenuOpt customMenuOpt;
}
