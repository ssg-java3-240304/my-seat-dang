package com.matdang.seatdang.menu.repository;


import com.matdang.seatdang.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
//    @Query("""
//        select
//            new com.matdang.seatdang.menu.entity.MenuList(
//            m.menus)
//        from
//            Store s left join MenuList m
//            on s.storeId = m.menuListId
//        where
//            s.storeId = :storeId
//
//    """)
    List<Menu> findByStoreId(@Param("storeId") Long storeId);
}
