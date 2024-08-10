package com.matdang.seatdang.menu.repository;


import com.matdang.seatdang.menu.entity.MenuList;
import com.matdang.seatdang.menu.vo.Menu;
import com.matdang.seatdang.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
public interface MenuRepository extends JpaRepository<Store, Long> {
    MenuList findByStoreId(Long storeId);
}
