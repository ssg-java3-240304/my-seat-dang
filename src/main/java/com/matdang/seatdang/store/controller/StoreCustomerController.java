package com.matdang.seatdang.store.controller;

import com.matdang.seatdang.menu.dto.MenuResponseDto;
import com.matdang.seatdang.menu.service.MenuService;
import com.matdang.seatdang.store.dto.StoreResponseDto;
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
        StoreResponseDto storeResponseDto = storeService.findByStoreId(storeId);
        List<MenuResponseDto> menus = menuService.findByStoreId(storeId);

        log.debug("store = {}", storeResponseDto);
        log.debug("menus = {}", menus);
        log.debug("thumbnail = {}", storeResponseDto.getThumbnail());
        log.debug("images = {}", storeResponseDto.getImages());

        model.addAttribute("store", storeResponseDto);
        model.addAttribute("menus", menus);
        model.addAttribute("thumbnail", storeResponseDto.getThumbnail());
        model.addAttribute("images", storeResponseDto.getImages());

        log.debug("storeType = {}", storeResponseDto.getStoreType());
        switch (storeResponseDto.getStoreType()) {
            case GENERAL_WAITING -> {return "customer/store/storeDetail";}
            case GENERAL_RESERVATION -> {return "customer/store/storeDetailReservation";}
            case CUSTOM -> {return "customer/store/storeDetailCustom";}
            default -> {throw new RuntimeException("페이지를 찾을수 없습니다");}
        }
    }
}
