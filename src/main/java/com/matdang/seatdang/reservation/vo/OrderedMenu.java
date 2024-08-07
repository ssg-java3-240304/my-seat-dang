package com.matdang.seatdang.reservation.vo;

import jakarta.persistence.Embeddable;

@Embeddable
public class OrderedMenu {
    private long menuId;
    private String menuName;
    private int menuPrice;
    private String imageUrl;
    private CustomMenuOpt customMenuOpt;
}
