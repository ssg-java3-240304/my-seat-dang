package com.matdang.seatdang.menu.repository;


import com.matdang.seatdang.menu.entity.MenuList;
import com.matdang.seatdang.menu.vo.Menu;
import com.matdang.seatdang.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MenuRepository extends JpaRepository<MenuList, Long> {
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
    MenuList findByStoreId(@Param("storeId") Long storeId);


    MenuList findByMenuListId(Long storeId);
}
