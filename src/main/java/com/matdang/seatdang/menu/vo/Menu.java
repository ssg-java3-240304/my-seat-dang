package com.matdang.seatdang.menu.vo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
//@DynamicUpdate // 수정된 필드만 업데이트
public class Menu {
//    @Id
//    @Column(name = "menu_id")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long menuId;
    @Column(name = "menu_name")
    private String menuName;
    @Column(name = "menu_price")
    private int menuPrice;
    @Column(name = "image")
    private String image;
    @Embedded
    private CustomMenuOpt customMenuOpt;
}