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
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;
import java.util.Set;

@Controller
@Slf4j
@RequestMapping("search")
@RequiredArgsConstructor
public class SearchController {
    private final MapService mapService;
    private final SearchStoreQueryService searchStoreQueryService;

    @GetMapping
    public String searchStore(
            @RequestParam(name = "store_name", required = false) String storeNameParam,
            @RequestParam(name = "store_address", required = false) String storeAddressParam,
            @PageableDefault(page = 1, size = 12) Pageable pageable,
            Model model) {

        log.debug("search store controller start storeName={}, storeAddress={}", storeNameParam, storeAddressParam);

        //안전하게 null 처리를 하기 위해 Optional을 사용합니다
        // ofNullable은 인자가 null인 경우 Optional.empty를 반환하고, Null인 아닌 경우 Optional로 래핑된 객체를 반환한다
        // filter는 null이나 "" 공백문자인 경우 필터링 처리를 하여 Optional.empty()를 반환 합니다.
        // Optional.empty()는 내부적으로 아무런 값이 없는 Optional 객체를 반환합니다.
        Optional<String> storeName = Optional.ofNullable(storeNameParam).filter(s -> !s.isEmpty());
        Optional<String> storeAddress = Optional.ofNullable(storeAddressParam).filter(s -> !s.isEmpty());

        Page<SearchStoreResponseDto> storePageResponse;
        model.addAttribute("ncpAccessId", mapService.getAccessId());

        if (storeName.isPresent() && storeAddress.isPresent()) {
            // 두 파라미터가 모두 제공된 경우
            log.debug("case: name&address | storeName={}, storeAddress={}", storeName, storeAddress);
            storePageResponse = searchStoreQueryService.searchStoreByNameAndAddress(storeName.get(), storeAddress.get(), pageable);
            model.addAttribute("storePage", storePageResponse.getContent());
        } else if (storeName.isPresent()) {
            // storeName만 제공된 경우
            storePageResponse = searchStoreQueryService.searchStoreByStoreName(storeName.get(), pageable);
            model.addAttribute("storePage", storePageResponse.getContent());
            log.debug("storePageResponse: {}", storePageResponse.getContent());
        } else if (storeAddress.isPresent()) {
            // storeAddress만 제공된 경우
            storePageResponse = searchStoreQueryService.searchStoreByAddress(storeAddress.get(), pageable);
            model.addAttribute("storePage", storePageResponse.getContent());
            log.debug("storePageResponse: {}", storePageResponse.getContent());
        } else {
            // 둘 다 제공되지 않은 경우
            return "customer/search/search";
        }



        return "customer/search/search";
    }
}
