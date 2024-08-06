package com.matdang.seatdang.store.controller;

import com.matdang.seatdang.common.paging.PageCriteria;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/store")
@RequiredArgsConstructor
@Slf4j
public class StoreController {
    private final StoreService storeService;

    @GetMapping("/storeList")
    public void storeList(@PageableDefault(page = 1, size = 10) Pageable pageable,
                          @RequestParam(required = false) String q,
                          Model model){
        log.info("GET/storeList?page={}", pageable.getPageNumber());
        // 1. 컨텐츠영역
        pageable = PageRequest.of(
                pageable.getPageNumber() - 1,
                pageable.getPageSize());

        Page<StoreListResponseDto> storePage = storeService.findAll(q, pageable);
        log.debug("storePage = {}", storePage.getContent());
        model.addAttribute("stores", storePage.getContent());

        // 2. 페이지바 영역
        int page = storePage.getNumber(); // 0-based 페이지번호
        int limit = storePage.getSize();
        int totalCount = (int) storePage.getTotalElements();
        String url = "storeList"; // 상대주소
        if(q != null)
            url += "?q=" + q;
        model.addAttribute("pageCriteria", new PageCriteria(page, limit, totalCount, url));
    }

    @GetMapping("/storeDetail")
    public void storeDetail(){
        log.info("GET/storeDetail");
    }
}
