package com.matdang.seatdang.menu.dto;

import com.matdang.seatdang.menu.entity.Menu;
import com.matdang.seatdang.menu.vo.CustomMenuOpt;
import com.matdang.seatdang.menu.vo.MenuStatus;
import com.matdang.seatdang.menu.vo.MenuType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuDto {
    private Long menuId;
    private Long storeId;
    private String menuName;
    private Integer menuPrice;
    private String image;
    private MenuType menuType;
    private MenuStatus menuStatus;
    private String menuDesc;
    private CustomMenuOpt customMenuOpt;

    public static MenuDto entityToDto(Menu menu) {
        return new MenuDto(
            menu.getMenuId(),
            menu.getStoreId(),
            menu.getMenuName(),
            menu.getMenuPrice(),
            menu.getImage(),
            menu.getMenuType(),
            menu.getMenuStatus(),
            menu.getMenuDesc(),
            menu.getCustomMenuOpt()
        );
    }
}
