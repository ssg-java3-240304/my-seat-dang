package com.matdang.seatdang.menu.controlloer;

import com.matdang.seatdang.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/menus")
@RequiredArgsConstructor
public class MenuController {
    private final MenuService menuService;

    @GetMapping("/{menuCode}")
    public ResponseEntity<?> findByMenuCode(@PathVariable("menuCode") Long menuCode){
        return menuService.findById(menuCode);
    }
}