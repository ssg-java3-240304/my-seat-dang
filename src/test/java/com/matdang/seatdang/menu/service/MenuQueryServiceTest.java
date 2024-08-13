package com.matdang.seatdang.menu.service;

import com.matdang.seatdang.menu.dto.MenuDetailResponseDto;
import com.matdang.seatdang.menu.entity.Menu;
import com.matdang.seatdang.menu.vo.MenuType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MenuQueryServiceTest {
    @Autowired
    MenuQueryService menuQueryService;

    @ValueSource(longs = {1L, 2L, 3L, 4L})
    @DisplayName("메뉴ID로 메뉴 조회하기")
    @ParameterizedTest
    public void test1(Long menuId) {
        //given
        //when
        MenuDetailResponseDto menu = menuQueryService.findByMenuId(menuId);
        //then
        assertThat(menu).isNotNull();
        assertThat(menu.getMenuId()).isEqualTo(menuId);
    }

    @DisplayName("매장ID로 메뉴 조회 성공")
    @ValueSource(longs = {2L, 3L, 4L})
    @ParameterizedTest
    public void test2(Long storeId) {
        //given
        //when
        List<MenuDetailResponseDto> menuSet = menuQueryService.findMenuSetByStoreId(storeId);
        System.out.println(menuSet.size());
        //then
        assertThat(menuSet).isNotNull();
        assertThat(menuSet).isNotEmpty();
        menuSet.stream().forEach(System.out::println);
    }

    @DisplayName("매장ID + 메뉴명 조회 성공")
    @ValueSource(longs = {2L, 3L, 4L})
    @ParameterizedTest
    public void test3(Long storeId) {
        //given
        List<MenuDetailResponseDto> givenMenuSet = menuQueryService.findMenuSetByStoreId(storeId);
        //when
        givenMenuSet.stream().forEach((menu)->{
            List<MenuDetailResponseDto> menuSet = menuQueryService.findMenuSetByStoreIdAndMenuName(storeId, menu.getMenuName());
            assertThat(menuSet).isNotNull();
            assertThat(menuSet).isNotEmpty();
            menuSet.stream().forEach((menu2) -> {
                assertThat(menu2.getMenuName()).contains(menu.getMenuName());
                assertThat(menu2.getStoreId()).isEqualTo(storeId);
            });
        });
    }
}