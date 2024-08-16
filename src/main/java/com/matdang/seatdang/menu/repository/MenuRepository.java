package com.matdang.seatdang.menu.repository;


import aj.org.objectweb.asm.commons.Remapper;
import com.matdang.seatdang.menu.entity.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    @Query("SELECT m FROM Menu m WHERE m.storeId = :storeId AND m.menuStatus <> 'DELETED'")
    List<Menu> findByStoreIdAndMenuStatusNotDeleted(@Param("storeId") Long storeId);

    @Query("SELECT m FROM Menu m WHERE m.storeId = :storeId AND m.menuStatus <> 'DELETED'")
    Page<Menu> findByStoreIdAndMenuStatusNotDeleted(@Param("storeId") Long storeId, Pageable pageable);

    List<Menu> findByStoreId(Long storeId);
    Page<Menu> findByStoreId(Long storeId, Pageable pageable);

    List<Menu> findByStoreIdAndMenuName(Long storeId, String menuName);

    List<Menu> findByStoreIdAndMenuNameContaining(Long storeId, String menuName);
}
