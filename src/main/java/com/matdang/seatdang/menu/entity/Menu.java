package com.matdang.seatdang.menu.entity;

import com.matdang.seatdang.menu.dto.MenuDto;
import com.matdang.seatdang.menu.vo.CustomMenuOpt;
import com.matdang.seatdang.menu.vo.MenuStatus;
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
    @Column(name="menu_status")
    @Enumerated(EnumType.STRING)
    private MenuStatus menuStatus;
    @Column(name="menu_desc")
    private String menuDesc;
    @Embedded
    private CustomMenuOpt customMenuOpt;


    public void update(MenuDto dto) {
        if (dto.getMenuName() != null) {
            this.menuName = dto.getMenuName();
        }
        if (dto.getMenuPrice() != null) {
            this.menuPrice = dto.getMenuPrice();
        }
        if (dto.getImage() != null) {
            this.image = dto.getImage();
        }
        if (dto.getMenuStatus() != null) {
            this.menuStatus = dto.getMenuStatus();
        }
        if (dto.getMenuDesc() != null) {
            this.menuDesc = dto.getMenuDesc();
        }
    }

    public void changeStatusToDelete(Long menuId) {
        if (this.menuId != null) {
            this.menuStatus = MenuStatus.DELETED;
        }
    }
}