package com.matdang.seatdang.menu.service;

import com.matdang.seatdang.menu.dto.MenuRequestDto;
import com.matdang.seatdang.menu.entity.Menu;
import com.matdang.seatdang.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuCommandService {
    private final MenuRepository menuRepository;

    public void regist(MenuRequestDto menuRequestDto) {
        Menu menu = Menu.builder()
                .storeId(menuRequestDto.getStoreId())
                .menuName(menuRequestDto.getMenuName())
                .menuPrice(menuRequestDto.getMenuPrice())
                .image(menuRequestDto.getImage())
                .menuType(menuRequestDto.getMenuType())
                .menuDesc(menuRequestDto.getMenuDesc())
                .customMenuOpt(menuRequestDto.getCustomMenuOpt())
                .build();
        menuRepository.save(menu);
    }
}
