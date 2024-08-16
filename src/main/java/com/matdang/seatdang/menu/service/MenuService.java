package com.matdang.seatdang.menu.service;

import com.matdang.seatdang.menu.dto.MenuResponseDto;
import com.matdang.seatdang.menu.entity.Menu;
import com.matdang.seatdang.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuService {
    private final MenuRepository menuRepository;

//    public ResponseEntity<?> findById(Long menuCode) {
//        Menu menu = menuRepository.findById(menuCode).orElse(null);
//        if(menu == null)
//            return ResponseEntity.notFound().build();
//        return ResponseEntity.ok(menu);
//    }

    public List<MenuResponseDto> findByStoreId(Long storeId) {
        List<Menu> menus = menuRepository.findByStoreId(storeId);
        return MenuResponseDto.fromMenuList(menus);
    }
}