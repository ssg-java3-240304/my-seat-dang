package com.matdang.seatdang.menu.repository;


import com.matdang.seatdang.menu.entity.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    Set<Menu> findByStoreId(Long storeId);
    Page<Menu> findByStoreId(Long storeId, Pageable pageable);
}
