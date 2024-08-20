package com.matdang.seatdang.waiting.controller;

import com.matdang.seatdang.store.entity.Store;
import com.matdang.seatdang.store.repository.StoreRepository;
import com.matdang.seatdang.waiting.controller.dto.WaitingRequestDto;
import com.matdang.seatdang.waiting.entity.Waiting;
import com.matdang.seatdang.waiting.repository.WaitingRepository;
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
    private final WaitingRepository waitingRepository;
    private final StoreRepository storeRepository;

    /**
     * TODO : 삭제 필요
     * defaultValue는 test 용도임
     */
    @GetMapping("/waiting")
    public String readyWaiting(@RequestParam(defaultValue = "1") Long storeId, Model model) {
        Store store = storeRepository.findByStoreId(storeId);

        model.addAttribute("waitingTeam", waitingRepository.countWaitingByStoreIdAndWaitingStatus(storeId));
        model.addAttribute("storeName", store.getStoreName());
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

    // TODO : 취소 후 url에 접속 못하게 막기(if문 상태처리)
    @GetMapping("/waiting/awaiting/detail")
    public String showWaitingDetail(@RequestParam(defaultValue = "1") Long waitingId, Model model) {
        Waiting waiting = waitingRepository.findById(waitingId).get();
        Store store = storeRepository.findByStoreId(waiting.getStoreId());

        model.addAttribute("waitingId", waitingId);
        model.addAttribute("waitingNumber", waiting.getWaitingNumber());
        model.addAttribute("waitingStatus", waiting.getWaitingStatus());
        model.addAttribute("createdDate", waiting.getCreatedDate());
        model.addAttribute("peopleCount", waiting.getCustomerInfo().getPeopleCount());
        model.addAttribute("storeName", store.getStoreName());


        return "customer/waiting/awaiting-waiting-detail";
    }

    @PostMapping("/waiting/awaiting/detail")
    public String cancelWaiting(@RequestParam(defaultValue = "1") Long waitingId, Model model) {

        int result = waitingRepository.cancelWaitingByCustomer(waitingId);
        if (result == 1) {
            log.info("=== 웨이팅 고객 취소 ===");
        } else log.error("== 웨이팅 고객 취소 오류 ===");

        return "redirect:/my-seat-dang/waiting/canceled/detail";
    }

    // TODO : Waiting entity 취소 시간 필드 추가
    @GetMapping("/waiting/canceled/detail")
    public String showCancelWaitingDetail(@RequestParam(defaultValue = "1") Long waitingId, Model model) {
        Waiting waiting = waitingRepository.findById(waitingId).get();
        Store store = storeRepository.findByStoreId(waiting.getStoreId());

        model.addAttribute("waitingNumber", waiting.getWaitingNumber());
        model.addAttribute("waitingStatus", waiting.getWaitingStatus());
        model.addAttribute("createdDate", waiting.getCreatedDate());
        model.addAttribute("peopleCount", waiting.getCustomerInfo().getPeopleCount());
        model.addAttribute("storeName", store.getStoreName());

        return "customer/waiting/canceled-waiting-detail";
    }
}
