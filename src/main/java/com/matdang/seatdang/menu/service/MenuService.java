package com.matdang.seatdang.menu.service;

import com.matdang.seatdang.menu.entity.GeneralMenu;
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

    public ResponseEntity<?> findById(Long menuCode) {
        GeneralMenu generalMenu = menuRepository.findById(menuCode).orElse(null);
        if(generalMenu == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(generalMenu);
    }
}
