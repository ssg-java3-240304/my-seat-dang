package com.matdang.seatdang.menu.dto;

import com.matdang.seatdang.menu.entity.Menu;
import com.matdang.seatdang.menu.vo.CustomMenuOpt;
import com.matdang.seatdang.menu.vo.MenuStatus;
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
    private Integer menuPrice;
    private String image;
    private MenuType menuType;
    private MenuStatus menuStatus;
    private String menuDesc;
    private CustomMenuOpt customMenuOpt;

    public void setMenuPrice(String menuPrice) {
        if (menuPrice == null || menuPrice.trim().isEmpty()) {
            this.menuPrice = null; //메뉴 값이 없으면 null 처리
        } else {
            this.menuPrice = Integer.valueOf(menuPrice);
        }
    }

    public void setMenuStatus(String menuStatus) {
        if(menuStatus == null || menuStatus.trim().isEmpty()) {
            this.menuStatus = MenuStatus.UNORDERABLE;
        }else if(menuStatus.equals("주문가능")){
            this.menuStatus = MenuStatus.ORDERABLE;
        }else if(menuStatus.equals("주문불가")){
            this.menuStatus = MenuStatus.UNORDERABLE;
        }else{
            this.menuStatus = MenuStatus.UNORDERABLE;
        }
    }

    public MenuDto toMenuDto() {
        return new MenuDto(
                this.menuId,
                this.storeId,
                this.menuName,
                this.menuPrice,
                this.image,
                this.menuType,
                this.menuStatus,
                this.menuDesc,
                this.customMenuOpt
        );
    }
}
