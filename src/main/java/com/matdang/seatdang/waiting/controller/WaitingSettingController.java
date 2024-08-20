package com.matdang.seatdang.waiting.controller;

import com.matdang.seatdang.auth.service.AuthService;
import com.matdang.seatdang.store.repository.StoreRepository;
import com.matdang.seatdang.store.repository.query.dto.AvailableWaitingTime;
import com.matdang.seatdang.waiting.service.WaitingSettingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalTime;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/store-owner/setting/waiting")
public class WaitingSettingController {

    private final StoreRepository storeRepository;
    private final WaitingSettingService waitingSettingService;
    private final AuthService authService;

    @GetMapping
    public String showSettings(Model model) {
        Long storeId = authService.getAuthenticatedStoreId();

        // 각 섹션에 필요한 데이터를 모델에 추가
        model.addAttribute("availableWaitingTime", waitingSettingService.findAvailableWaitingTime(storeId));
        model.addAttribute("estimatedWaitingTime", waitingSettingService.findEstimatedWaitingTime(storeId).getMinute());
        model.addAttribute("waitingPeopleCount", waitingSettingService.findWaitingPeopleCount(storeId));
        model.addAttribute("waitingStatus", waitingSettingService.findWaitingStatus(storeId));

        return "storeowner/setting/waiting-setting";
    }

    @PostMapping("/available-waiting-time")
    public String updateAvailableTime(@ModelAttribute AvailableWaitingTime availableWaitingTime) {
        Long storeId = authService.getAuthenticatedStoreId();
        log.debug("availableWaitingTime = {}", availableWaitingTime);

        log.debug("====== update ====");
        int result = storeRepository.updateWaitingAvailableTime(availableWaitingTime.getWaitingOpenTime(),
                availableWaitingTime.getWaitingCloseTime(), storeId);
        log.debug("==========");

        if (result == 1) {
            log.info("=== update available time ===");
        }

        return "redirect:/store-owner/setting/waiting";
    }

    @PostMapping("/estimated-waiting-time")
    public String updateEstimatedTime(int estimatedWaitingTime) {
        Long storeId = authService.getAuthenticatedStoreId();
        log.debug("localTime = {}", estimatedWaitingTime);

        log.debug("====== update ====");
        int result = storeRepository.updateEstimatedWaitingTime(LocalTime.of(0, estimatedWaitingTime), storeId);
        log.debug("==========");

        if (result == 1) {
            log.info("=== update estimated time ===");
        }

        return "redirect:/store-owner/setting/waiting";
    }

    @PostMapping("/waiting-status")
    public String changeWaitingStatus(@RequestParam int status) {
        Long storeId = authService.getAuthenticatedStoreId();
        int result = waitingSettingService.changeWaitingStatus(status, storeId);

        if (result>=1) {
            log.info("=== Change Waiting Status ===");
        }
        log.debug("result ={}", result);

        return "redirect:/store-owner/setting/waiting";
    }

    @PostMapping("/waiting-people-count")
    public String updatePeopleCount(int waitingPeopleCount) {
        Long storeId = authService.getAuthenticatedStoreId();

        log.debug("====== update ====");
        int result = storeRepository.updateWaitingPeopleCount(waitingPeopleCount, storeId);
        log.debug("==========");

        if (result == 1) {
            log.info("=== update waiting people count ===");
        }

        return "redirect:/store-owner/setting/waiting";
    }
}
