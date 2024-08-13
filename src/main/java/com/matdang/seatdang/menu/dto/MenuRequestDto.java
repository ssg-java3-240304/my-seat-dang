package com.matdang.seatdang.menu.dto;

import com.matdang.seatdang.menu.entity.Menu;
import com.matdang.seatdang.menu.vo.CustomMenuOpt;
import com.matdang.seatdang.menu.vo.MenuType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuRequestDto {
    private Long menuId;
    private Long storeId;
    private String menuName;
    private int menuPrice;
    private String image;
    private MenuType menuType;
    private String menuDesc;
    private CustomMenuOpt customMenuOpt;

    public static MenuRequestDto toDto(Menu menu) {
        return new MenuRequestDto(
                menu.getMenuId(),
                menu.getStoreId(),
                menu.getMenuName(),
                menu.getMenuPrice(),
                menu.getImage(),
                menu.getMenuType(),
                menu.getMenuDesc(),
                menu.getCustomMenuOpt()
        );
    }
}
