package com.matdang.seatdang.menu.repository;


import aj.org.objectweb.asm.commons.Remapper;
import com.matdang.seatdang.menu.entity.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByStoreId(Long storeId);
    Page<Menu> findByStoreId(Long storeId, Pageable pageable);

    List<Menu> findByStoreIdAndMenuName(Long storeId, String menuName);

    List<Menu> findByStoreIdAndMenuNameContaining(Long storeId, String menuName);
}
