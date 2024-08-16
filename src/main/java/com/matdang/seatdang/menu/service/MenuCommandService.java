package com.matdang.seatdang.menu.service;

import com.matdang.seatdang.auth.principal.StoreOwnerUserDetails;
import com.matdang.seatdang.menu.dto.MenuDto;
import com.matdang.seatdang.menu.entity.Menu;
import com.matdang.seatdang.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MenuCommandService {
    private final MenuRepository menuRepository;

    public void regist(MenuDto menuRequestDto) {
        log.debug("regist menu service / request: {}", menuRequestDto);
        //로그인 사용자 ID 식별 및 menuRequestDto에 세팅
        StoreOwnerUserDetails userDetails = (StoreOwnerUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("authentication Id = {}", userDetails.getId());

        Menu menu = Menu.builder()
                .storeId(menuRequestDto.getStoreId())
                .menuName(menuRequestDto.getMenuName())
                .menuPrice(menuRequestDto.getMenuPrice())
                .image(menuRequestDto.getImage())
                .menuType(menuRequestDto.getMenuType())
                .menuStatus(menuRequestDto.getMenuStatus())
                .menuDesc(menuRequestDto.getMenuDesc())
                .customMenuOpt(menuRequestDto.getCustomMenuOpt())
                .build();
        menuRepository.save(menu);
    }

    public void update(MenuDto menuRequestDto) {
        Menu menu = menuRepository.findById(menuRequestDto.getMenuId()).orElseThrow();
        log.debug("update menu service / request: {}", menuRequestDto.getMenuId());
        menu.update(menuRequestDto);
    }

    public void delete(Long menuId) {
        log.debug("delete menu service / menuId: {}", menuId);
        Menu menu = menuRepository.findById(menuId).orElseThrow();
        menu.changeStatusToDelete(menuId);
    }
}
