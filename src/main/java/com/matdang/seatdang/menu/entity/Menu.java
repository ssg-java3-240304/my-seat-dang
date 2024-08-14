package com.matdang.seatdang.menu.entity;

import com.matdang.seatdang.menu.dto.MenuRequestDto;
import com.matdang.seatdang.menu.vo.CustomMenuOpt;
import com.matdang.seatdang.menu.vo.MenuType;
import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
@Setter(AccessLevel.PRIVATE)
@Getter
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuId;
    @Column(name = "store_id")
    private Long storeId;
    @Column(name = "menu_name")
    private String menuName;
    @Column(name = "menu_price")
    private int menuPrice;
    @Column(name = "image")
    private String image;
    @Column(name = "menu_type")
    @Enumerated(EnumType.STRING)
    private MenuType menuType;
    @Column(name="menu_desc")
    private String menuDesc;
    @Embedded
    private CustomMenuOpt customMenuOpt;

    public void update(MenuRequestDto dto){
        this.menuName = dto.getMenuName();
        this.menuPrice = dto.getMenuPrice();
        this.image = dto.getImage();
        this.menuType = dto.getMenuType();
        this.menuDesc = dto.getMenuDesc();
        this.customMenuOpt = dto.getCustomMenuOpt();
    }
}