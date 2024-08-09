package com.matdang.seatdang.menu.controlloer;

import com.matdang.seatdang.menu.entity.MenuList;
import com.matdang.seatdang.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/store")
@RequiredArgsConstructor
@Slf4j
public class MenuController {
    private final MenuService menuService;

//    @GetMapping("/storeDetail")
//    public void menuDetail(@RequestParam("storeId") Long storeId, Model model){
//        MenuList menus = menuService.findAllByStoreId(storeId);
//        log.debug("menus = {}", menus);
//        model.addAttribute("menus", menus);
//    }

//    @GetMapping("/{menuCode}")
//    public ResponseEntity<?> findByMenuCode(@PathVariable("menuCode") Long menuCode){
//        return menuService.findById(menuCode);
//    }
}