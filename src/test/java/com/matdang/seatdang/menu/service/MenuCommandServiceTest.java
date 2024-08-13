package com.matdang.seatdang.menu.service;

import com.matdang.seatdang.menu.dto.MenuDetailResponseDto;
import com.matdang.seatdang.menu.dto.MenuRequestDto;
import com.matdang.seatdang.menu.entity.Menu;
import com.matdang.seatdang.menu.vo.MenuType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MenuCommandServiceTest {

    @Autowired
    MenuQueryService menuQueryService;
    @Autowired
    MenuCommandService menuCommandService;

    @DisplayName("jpa_create test")
    @Test
    public void test1() {
        //given
        //when
        //then
    }

    @DisplayName("메뉴 한건 저장하기")
    @Test
    public void test2() {
        //given
        Menu menu = Menu.builder()
                .storeId(2L)
                .menuName("생과일케이크")
                .menuPrice(100000)
                .image("https://cdn.paris.spl.li/wp-content/uploads/201126_%E1%84%92%E1%85%A9%E1%86%B7%E1%84%91%E1%85%A6%E1%84%8B%E1%85%B5%E1%84%8C%E1%85%B5%E1%84%8E%E1%85%A9%E1%84%8F%E1%85%A9%E1%84%89%E1%85%A9%E1%84%85%E1%85%A1%E1%84%88%E1%85%A1%E1%86%BC_01-1-1280x1280.jpg")
                .menuType(MenuType.NORMAL)
                .menuDesc("딸기 달달해 맛있어요")
                .customMenuOpt(null)
                .build();
        MenuRequestDto requestDto = MenuRequestDto.toDto(menu);
        //when
        menuCommandService.regist(requestDto);
        List<MenuDetailResponseDto> result = menuQueryService.findMenuSetByStoreId(2L);
        //then
        assertThat(result).isNotEmpty();
        result.forEach(menuDetailResponseDto ->
                assertThat(menuDetailResponseDto.getMenuId()).isNotNull()
        );
    }


}