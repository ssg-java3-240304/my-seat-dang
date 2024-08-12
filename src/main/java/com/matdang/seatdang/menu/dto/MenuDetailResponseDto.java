package com.matdang.seatdang.menu.dto;

import com.matdang.seatdang.menu.entity.Menu;
import com.matdang.seatdang.menu.vo.CustomMenuOpt;
import com.matdang.seatdang.menu.vo.MenuType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuDetailResponseDto {
    private Long menuId;
    private Long storeId;
    private String menuName;
    private int menuPrice;
    private String image;
    private MenuType menuType;
    private String menuDesc;
    private CustomMenuOpt customMenuOpt;

    public static MenuDetailResponseDto toDto(Menu menu) {
        return new MenuDetailResponseDto(
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
