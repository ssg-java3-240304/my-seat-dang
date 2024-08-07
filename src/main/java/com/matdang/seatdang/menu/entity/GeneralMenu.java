package com.matdang.seatdang.menu.entity;

import com.matdang.seatdang.menu.vo.MenuType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;

@Entity(name = "general_menu")
@Table(name = "general_menu")
@Embeddable
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "menu_type") // 자식클래스 타입을 결정하는 컬럼명
@Data
@NoArgsConstructor
@AllArgsConstructor
//@DynamicUpdate // 수정된 필드만 업데이트
public class GeneralMenu {
    @Id
    @Column(name = "menu_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuId;
    @Column(name = "menu_name")
    private String menuName;
    @Column(name = "menu_price")
    private int menuPrice;
    @Column(name = "image")
    private String image;
    @Column(name = "menu_type", insertable=false, updatable=false)
    @Enumerated(EnumType.STRING)
    private MenuType menuType;
}