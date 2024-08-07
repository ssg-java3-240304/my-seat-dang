package com.matdang.seatdang.menu.repository;


import com.matdang.seatdang.menu.entity.GeneralMenu;
import org.springframework.data.jpa.repository.JpaRepository;
public interface MenuRepository extends JpaRepository<GeneralMenu, Long> {
}
