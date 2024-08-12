package com.matdang.seatdang.menu.service;

import com.matdang.seatdang.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuCommandService {
    MenuRepository menuRepository;

}
