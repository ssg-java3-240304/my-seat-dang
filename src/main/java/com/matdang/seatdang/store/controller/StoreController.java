package com.matdang.seatdang.store.controller;

import com.matdang.seatdang.common.paging.PageCriteria;
import com.matdang.seatdang.menu.entity.MenuList;
import com.matdang.seatdang.menu.service.MenuService;
import com.matdang.seatdang.store.dto.StoreListResponseDto;
import com.matdang.seatdang.store.entity.Store;
import com.matdang.seatdang.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.matdang.seatdang.common.storeEnum.StoreType.CUSTOM;
import static com.matdang.seatdang.common.storeEnum.StoreType.GENERAL_RESERVATION;

@Controller
@RequestMapping("/customer")
@RequiredArgsConstructor
@Slf4j
public class StoreController {
    private final StoreService storeService;
    private final MenuService menuService;

    @GetMapping("/store/storeList")
    public void storeList(@PageableDefault(page = 1, size = 10) Pageable pageable,
                          Model model){
        log.info("GET/storeList?page={}", pageable.getPageNumber());
        // 1. 컨텐츠영역
        pageable = PageRequest.of(
                pageable.getPageNumber() - 1,
                pageable.getPageSize());

        Page<StoreListResponseDto> storePage = storeService.findAll(pageable);
        log.debug("storePage = {}", storePage.getContent());
        model.addAttribute("stores", storePage.getContent());

        // 2. 페이지바 영역
        int page = storePage.getNumber(); // 0-based 페이지번호
        int limit = storePage.getSize();
        int totalCount = (int) storePage.getTotalElements();
        String url = "storeList"; // 상대주소
//        if(q != null)
//            url += "?q=" + q;
        model.addAttribute("pageCriteria", new PageCriteria(page, limit, totalCount, url));
    }

    @GetMapping("/storeReservationList")
    public void storeReservationList(@PageableDefault(page = 1, size = 10) Pageable pageable,
                          Model model){
        log.info("GET/storeReservationList?page={}", pageable.getPageNumber());
        // 1. 컨텐츠영역
        pageable = PageRequest.of(
                pageable.getPageNumber() - 1,
                pageable.getPageSize());

        Page<StoreListResponseDto> storePage = storeService.findByStoreTypeContaining(GENERAL_RESERVATION, CUSTOM, pageable);
        log.debug("storePage = {}", storePage.getContent());
        model.addAttribute("stores", storePage.getContent());

        // 2. 페이지바 영역
        int page = storePage.getNumber(); // 0-based 페이지번호
        int limit = storePage.getSize();
        int totalCount = (int) storePage.getTotalElements();
        String url = "storeList"; // 상대주소
//        if(q != null)
//            url += "?q=" + q;
        model.addAttribute("pageCriteria", new PageCriteria(page, limit, totalCount, url));
    }

    @GetMapping("/storeWaitingList")
    public void storeWaitingList(@PageableDefault(page = 1, size = 10) Pageable pageable,
                                     Model model){
        log.info("GET/storeWaitingList?page={}", pageable.getPageNumber());
        // 1. 컨텐츠영역
        pageable = PageRequest.of(
                pageable.getPageNumber() - 1,
                pageable.getPageSize());

        Page<StoreListResponseDto> storePage = storeService.findAll(pageable);
        log.debug("storePage = {}", storePage.getContent());
        model.addAttribute("stores", storePage.getContent());

        // 2. 페이지바 영역
        int page = storePage.getNumber(); // 0-based 페이지번호
        int limit = storePage.getSize();
        int totalCount = (int) storePage.getTotalElements();
        String url = "storeList"; // 상대주소
//        if(q != null)
//            url += "?q=" + q;
        model.addAttribute("pageCriteria", new PageCriteria(page, limit, totalCount, url));
    }

    @GetMapping("/store/detail")
    public void storeDetail(@RequestParam("storeId") Long storeId, Model model){
        Store store = storeService.findByStoreId(storeId);
//        MenuList menus = menuService.findByMenuListId();
        MenuList menus = menuService.findByMenuListId(storeId);
        log.debug("store = {}", store);
        log.debug("menus = {}", menus);
        model.addAttribute("store", store);
        model.addAttribute("menus", menus);
    }
}
