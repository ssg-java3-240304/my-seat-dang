package com.matdang.seatdang.search.controller;

import com.matdang.seatdang.common.config.NcpMapConfig;
import com.matdang.seatdang.search.dto.StoreSearchResponseDto;
import com.matdang.seatdang.search.service.MapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashSet;
import java.util.Set;

@Controller
@Slf4j
@RequestMapping("search")
@RequiredArgsConstructor
public class SearchController {
    private final MapService mapService;

    @GetMapping
    public String index(Model model) {
        Set<StoreSearchResponseDto> storeSearchResponseDtoSet = CreateDummyStoreList();
        model.addAttribute("storeSearchResponseSet", storeSearchResponseDtoSet);
        model.addAttribute("ncpAccessId", mapService.getAccessId());
//        model.addAttribute("ncpSecretKey", mapService.getSecretKey());
        return "customer/search/search";
    }

    public Set<StoreSearchResponseDto> CreateDummyStoreList(){
        Set<StoreSearchResponseDto> storeSearchResponseDtoSet = new HashSet<>();
//        for(int i = 0; i<5; i++){
//            StoreSearchResponseDto storeSearchResponseDto = StoreSearchResponseDto.builder()
//                    .storeId(i)
//                    .storeName("맛있당"+i+"호점")
//                    .storeAddress("서울 강남구 삼성로 534 싹아트센터")
//                    .build();
//            storeSearchResponseDtoSet.add(storeSearchResponseDto);
//        }
        storeSearchResponseDtoSet.add(StoreSearchResponseDto.builder()
                .storeId(1)
                .storeName("맛있당1호점")
                .storeAddress("삼성동 158-12")
                .build());
        storeSearchResponseDtoSet.add(StoreSearchResponseDto.builder()
                .storeId(2)
                .storeName("맛있당2호점")
                .storeAddress("서울 강남구 테헤란로83길 14 1층 106호")
                .build());
        storeSearchResponseDtoSet.add(StoreSearchResponseDto.builder()
                .storeId(3)
                .storeName("맛있당3호점")
                .storeAddress("서울 강남구 삼성로 512 삼성동빌딩 19층")
                .build());
        storeSearchResponseDtoSet.add(StoreSearchResponseDto.builder()
                .storeId(4)
                .storeName("맛있당4호점")
                .storeAddress("서울 강남구 테헤란로81길 35")
                .build());
        return storeSearchResponseDtoSet;
    }
}
