package com.matdang.seatdang.menu.entity;

import com.matdang.seatdang.menu.vo.Menu;
import com.matdang.seatdang.menu.vo.MenuType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name = "menu_list")
@Table(name = "menu_list")
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class MenuList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_list_id")
    private Long menuListId;

    @ElementCollection
    @CollectionTable(
            name = "menu", joinColumns = @JoinColumn(name = "menu_list_id")
    )
    @Column(name = "menus")
    private List<Menu> menus;
//    @Column(name = "menu_type", insertable=false, updatable=false)
    @Column(name = "menu_type")
    @Enumerated(EnumType.STRING)
    private MenuType menuType;

    @Column(name = "store_id")
    private Long storeId;
}
