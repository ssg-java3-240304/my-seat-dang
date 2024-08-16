package com.matdang.seatdang.menu.service;


import com.matdang.seatdang.menu.dto.MenuDto;
import com.matdang.seatdang.menu.entity.Menu;
import com.matdang.seatdang.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuQueryService {
    private final MenuRepository menuRepository;

    public List<MenuDto> findMenuSetByStoreId(Long storeId) {
        List<Menu> menus = menuRepository.findByStoreId(storeId);
        return menus.stream().map(MenuDto::entityToDto).collect(Collectors.toList());
    }

    public Page<MenuDto> findMenuPageByStoreId(Long storeId, Pageable pageable) {
        return menuRepository.findByStoreId(storeId, pageable).map(MenuDto::entityToDto);
    }

    public Page<MenuDto>  findMenuPageByStoreIdNotDeleted(Long storeId, Pageable pageable) {
        return menuRepository.findByStoreIdAndMenuStatusNotDeleted(storeId, pageable).map(MenuDto::entityToDto);
    }

    public MenuDto findByMenuId(Long menuId) {
        return menuRepository.findById(menuId)
                .map(MenuDto::entityToDto)
                .orElse(null);
    }

    public List<MenuDto> findMenuSetByStoreIdAndMenuName(Long storeId, String menuName) {
        List<Menu> menus = menuRepository.findByStoreIdAndMenuNameContaining(storeId, menuName);
        return menus.stream().map(MenuDto::entityToDto).collect(Collectors.toList());
    }

}
