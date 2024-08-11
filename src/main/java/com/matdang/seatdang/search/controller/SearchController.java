package com.matdang.seatdang.search.controller;

import com.matdang.seatdang.search.dto.SearchStoreResponseDto;
import com.matdang.seatdang.search.service.MapService;
import com.matdang.seatdang.search.service.SearchStoreQueryService;
import com.matdang.seatdang.store.dto.StoreListResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Set;

@Controller
@Slf4j
@RequestMapping("search")
@RequiredArgsConstructor
public class SearchController {
    private final MapService mapService;
    private final SearchStoreQueryService searchStoreQueryService;

    @GetMapping
    public String searchStore(Model model) {
        log.debug("search store controller start");
        int pageNumber = 0;
        int pageSize = 12;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        String storeAddress = "삼성";
        Page<SearchStoreResponseDto> storePageResponse = searchStoreQueryService.searchStoreByAddress(storeAddress, pageable);

        model.addAttribute("storePage", storePageResponse.getContent());
        model.addAttribute("ncpAccessId", mapService.getAccessId());
        log.debug("storePageResponse: {}", storePageResponse.getContent());
//        model.addAttribute("ncpSecretKey", mapService.getSecretKey());
        return "customer/search/search";
    }
}
