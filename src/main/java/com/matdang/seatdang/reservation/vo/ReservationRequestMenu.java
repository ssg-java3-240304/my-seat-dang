package com.matdang.seatdang.reservation.vo;

import com.matdang.seatdang.menu.vo.MenuType;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequestMenu {
    private String menuName;
    private int menuPrice;
    private String imageUrl;
    private MenuType menuType;
    private CustomMenuOpt customMenuOpt;
    private int quantity;
}
