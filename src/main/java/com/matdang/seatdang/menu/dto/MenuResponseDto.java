package com.matdang.seatdang.menu.dto;

import com.matdang.seatdang.menu.entity.Menu;
import com.matdang.seatdang.menu.vo.CustomMenuOpt;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuResponseDto {
    private Long menuId;
    private String menuName;
    private int menuPrice;
    private String image;
    private String menuDesc;
    @Embedded
    private CustomMenuOpt customMenuOpt;
    private Long storeId;

    // 단일 Menu 객체를 MenuResponseDto로 변환
    public static MenuResponseDto fromMenu(Menu menu) {
        return new MenuResponseDto(
                menu.getMenuId(),
                menu.getMenuName(),
                menu.getMenuPrice(),
                menu.getImage(),
                menu.getMenuDesc(),
                menu.getCustomMenuOpt(),
                menu.getStoreId()
        );
    }

    // List<Menu> 객체를 List<MenuResponseDto>로 변환
    public static List<MenuResponseDto> fromMenuList(List<Menu> menus) {
        return menus.stream()
                .map(MenuResponseDto::fromMenu)
                .collect(Collectors.toList());
    }
}
