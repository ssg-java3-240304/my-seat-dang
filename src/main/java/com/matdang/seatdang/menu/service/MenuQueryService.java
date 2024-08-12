package com.matdang.seatdang.menu.service;


import com.matdang.seatdang.menu.dto.MenuDetailResponseDto;
import com.matdang.seatdang.menu.entity.Menu;
import com.matdang.seatdang.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuQueryService {
    private final MenuRepository menuRepository;

    public Set<MenuDetailResponseDto> findMenuSetByStoreId(Long storeId) {
        Set<Menu> menus = menuRepository.findByStoreId(storeId);
        return menus.stream().map(MenuDetailResponseDto::toDto).collect(Collectors.toSet());
    }

    public Page<MenuDetailResponseDto> findMenuPageByStoreId(Long storeId, Pageable pageable) {
        return menuRepository.findByStoreId(storeId, pageable).map(MenuDetailResponseDto::toDto);
    }

    public MenuDetailResponseDto findByMenuId(Long menuId) {
        return menuRepository.findById(menuId)
                .map(MenuDetailResponseDto::toDto)
                .orElse(null);
    }

}
