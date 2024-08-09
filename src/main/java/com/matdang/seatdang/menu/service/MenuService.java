package com.matdang.seatdang.menu.service;

import com.matdang.seatdang.menu.entity.MenuList;
import com.matdang.seatdang.menu.vo.Menu;
import com.matdang.seatdang.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public MenuList findAllByStoreId(Long storeId) {
        return menuRepository.findAllByStoreId(storeId);
    }
}
