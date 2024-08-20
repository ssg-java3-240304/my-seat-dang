package com.matdang.seatdang.waiting.controller;

import com.matdang.seatdang.store.entity.Store;
import com.matdang.seatdang.store.repository.StoreRepository;
import com.matdang.seatdang.waiting.controller.dto.WaitingRequestDto;
import com.matdang.seatdang.waiting.service.WaitingCustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/my-seat-dang")
@RequiredArgsConstructor
public class WaitingCustomerController {
    private final WaitingCustomerService waitingCustomerService;
    private final StoreRepository storeRepository;

    /**
     * TODO : 삭제 필요
     * defaultValue는 test 용도임
     */
    @GetMapping("/waiting")
    public String readyWaiting(@RequestParam(defaultValue = "1") Long storeId, Model model) {
        Store store = storeRepository.findByStoreId(storeId);
        model.addAttribute("waitingPeopleCount", store.getStoreSetting().getWaitingPeopleCount());
        model.addAttribute("storeId", storeId);
        return "customer/waiting/waiting";
    }

    @PostMapping("/waiting")
    public String createWaiting(@ModelAttribute WaitingRequestDto waitingRequestDto) {
        log.debug("=== create Waiting ===");
        waitingCustomerService.createWaiting(waitingRequestDto.getStoreId(), waitingRequestDto.getPeopleCount());

        /**
         * TODO : 변경 필요
         * 현재 등록한 웨이팅 상세페이지로 이동
         */
        return "redirect:/my-seat-dang/waiting";
    }
}
