package com.matdang.seatdang.menu.repository;


import com.matdang.seatdang.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
public interface MenuRepository extends JpaRepository<Menu, Long> {
}
