package com.matdang.seatdang.waiting.controller;

import com.matdang.seatdang.auth.service.AuthService;
import com.matdang.seatdang.store.repository.StoreRepository;
import com.matdang.seatdang.store.repository.query.dto.AvailableWaitingTime;
import com.matdang.seatdang.waiting.service.WaitingSettingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/store/setting")
public class WaitingSettingController {

    private final StoreRepository storeRepository;
    private final WaitingSettingService waitingSettingService;
    private final AuthService authService;

    @GetMapping("/available-waiting-time")
    public String availableTimeSetting(Model model) {
        Long storeId = authService.getAuthenticatedStoreId();
        model.addAttribute("availableWaitingTime", waitingSettingService.findAvailableWaitingTime(storeId));

        return "store/setting/available-waiting-time";
    }

    @PostMapping("/available-waiting-time")
    public String updateAvailableTime(@ModelAttribute AvailableWaitingTime availableWaitingTime) {
        Long storeId = authService.getAuthenticatedStoreId();

        log.debug("====== update ====");
        int result = storeRepository.updateWaitingAvailableTime(availableWaitingTime.getWaitingOpenTime(),
                availableWaitingTime.getWaitingCloseTime(), storeId);
        log.debug("==========");


        if (result == 1) {
            log.info("=== update available time ===");
        }

        return "redirect:/store/setting/available-waiting-time";
    }
}
