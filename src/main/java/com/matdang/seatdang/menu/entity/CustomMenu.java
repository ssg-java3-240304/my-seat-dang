package com.matdang.seatdang.menu.entity;

import com.matdang.seatdang.menu.vo.MenuType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity(name = "custom_menu")
@Table(name = "custom_menu")
@DiscriminatorValue("custom_menu")
@Data
@NoArgsConstructor
@ToString(callSuper = true)
public class CustomMenu extends GeneralMenu {
    private String sheet;
    private String size;
    private String cream;
    private String lettering;

    public CustomMenu(Long menuId, String menuName, int menuPrice, String image, MenuType menuType, String sheet, String size, String cream, String lettering) {
        super(menuId, menuName, menuPrice, image, menuType);
        this.sheet = sheet;
        this.size = size;
        this.cream = cream;
        this.lettering = lettering;
    }
}
