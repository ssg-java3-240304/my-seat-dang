package com.matdang.seatdang.search.service;

import com.matdang.seatdang.search.dto.SearchStoreResponseDto;
import com.matdang.seatdang.store.dto.StoreListResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SearchStoreQueryServiceTest {
    @Autowired
    private SearchStoreQueryService searchStoreQueryService;

    @DisplayName("searchStoreByStoreName")
    @ParameterizedTest
    @ValueSource(strings = {"마카롱", "소라빵", "에클레어", "케이크"})
    public void test1(String storeName) {
        //given
        int pageNumber = 0;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        //when
        Page<SearchStoreResponseDto> storePage = searchStoreQueryService.searchStoreByStoreName(storeName, pageable);
        //then
        System.out.println(storePage.getNumberOfElements());
        assertThat(storePage.getNumberOfElements()).isEqualTo(pageSize);
        assertThat(storePage.getContent()).allMatch((store)->store.getStoreName().contains(storeName));
    }

    @DisplayName("searchStoreByAddress")
    @ParameterizedTest
    @ValueSource(strings = {"삼성", "노량진", "용산", "선릉"})
    public void test2(String storeAddress) {
        //given
        int pageNumber = 0;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        //when
        Page<SearchStoreResponseDto> storePage = searchStoreQueryService.searchStoreByStoreName(storeAddress, pageable);
        //then
        System.out.println(storePage.getNumberOfElements());
        storePage.getContent().stream().forEach((store)-> System.out.println(store.getStoreAddress()));
        assertThat(storePage.getNumberOfElements()).isEqualTo(pageSize);
        assertThat(storePage.getContent()).allMatch((store)->store.getStoreName().contains(storeAddress));
    }


}