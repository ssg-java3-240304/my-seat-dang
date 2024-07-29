package com.matdang.seatdang.menu.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

/**
 * <pre>
 * @Entity
 *  - Entity의 이름(JPA 영속성컨텍스트 상의 이름)
 *  - 영속성컨텍스트안에 동일한 이름의 엔티티가 있을수 없다.
 *  - @Id필드를 반드시 지정해야 한다.
 *
 * @Table
 *  - Entity와 매칭되는 테이블명을 작성
 * <pre>
 */
@Entity(name = "Menu")
@Table(name = "tbl_menu")
@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate // 수정된 필드만 업데이트
public class Menu {
    @Id
    @Column(name = "menu_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY) // pk컬럼값 생성을 DB에 위임(mysql는 auto_increment처리)
    private Long menuCode;
    @Column(name = "menu_name")
    private String menuName;
    @Column(name = "menu_price")
    private int menuPrice;
    @Column(name = "category_code")
    private Long categoryCode; // null값이 존재할 수 있으므로, 기본형이 아닌 wrapper타입을 사용한다.
    @Column(name = "orderable_status")
    private String orderableStatus;
}