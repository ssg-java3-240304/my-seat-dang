package com.matdang.seatdang.store.controller;

import com.matdang.seatdang.menu.dto.MenuResponseDto;
import com.matdang.seatdang.menu.service.MenuService;
import com.matdang.seatdang.store.entity.Store;
import com.matdang.seatdang.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/my-seat-dang/store")
@RequiredArgsConstructor
@Slf4j
public class StoreCustomerController {
    private final StoreService storeService;
    private final MenuService menuService;

    @GetMapping("/detail/{storeId}")
    public String detail(@PathVariable Long storeId, Model model){
        Store store = storeService.findByStoreId(storeId);
        List<MenuResponseDto> menus = menuService.findByStoreId(storeId);

        log.debug("store = {}", store);
        log.debug("menus = {}", menus);
        log.debug("thumbnail = {}", store.getThumbnail());
        log.debug("images = {}", store.getImages());

        model.addAttribute("store", store);
        model.addAttribute("menus", menus);
        model.addAttribute("thumbnail", store.getThumbnail());
        model.addAttribute("images", store.getImages());

        return "customer/store/storeDetail";
    }
}
