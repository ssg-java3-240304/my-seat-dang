package com.matdang.seatdang.waiting.controller;

import com.matdang.seatdang.auth.service.AuthService;
import com.matdang.seatdang.store.entity.Store;
import com.matdang.seatdang.store.repository.StoreRepository;
import com.matdang.seatdang.waiting.controller.dto.AwaitingWaitingResponse;
import com.matdang.seatdang.waiting.controller.dto.CanceledWaitingResponse;
import com.matdang.seatdang.waiting.controller.dto.ReadyWaitingResponse;
import com.matdang.seatdang.waiting.controller.dto.VisitedWaitingResponse;
import com.matdang.seatdang.waiting.controller.dto.WaitingRequest;
import com.matdang.seatdang.waiting.entity.Waiting;
import com.matdang.seatdang.waiting.repository.WaitingRepository;
import com.matdang.seatdang.waiting.repository.query.WaitingQueryRepository;
import com.matdang.seatdang.waiting.repository.query.dto.WaitingInfoDto;
import com.matdang.seatdang.waiting.service.WaitingCustomerService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/my-seat-dang")
@RequiredArgsConstructor
public class WaitingCustomerController {
    private final WaitingCustomerService waitingCustomerService;
    private final WaitingQueryRepository waitingQueryRepository;
    private final WaitingRepository waitingRepository;
    private final StoreRepository storeRepository;
    private final AuthService authService;

    @GetMapping("/test-store")
    public String showStore(@RequestParam(defaultValue = "1") Long storeId, Model model) {
        Long memberId = authService.getAuthenticatedMember().getMemberId();
        boolean isRegistered = waitingRepository.isRegisteredWaiting(storeId, memberId);

        model.addAttribute("storeId", storeId);
        model.addAttribute("isRegistered", isRegistered);

        return "customer/waiting/test-store";
    }

    /**
     * TODO : 삭제 필요
     * defaultValue는 test 용도임
     *
     * TODO : URL 변경
     */
    // TODO : 웨이팅 후 다시 주소 접근 차단
    @GetMapping("/waiting/{storeId}")
    public String readyWaiting(@PathVariable Long storeId, Model model) {
        Store store = storeRepository.findByStoreId(storeId);

        model.addAttribute("waitingTeam", waitingRepository.countWaitingByStoreIdAndWaitingStatus(storeId));
        model.addAttribute("readyWaitingResponse", ReadyWaitingResponse.create(store));

        return "customer/waiting/registration";
    }

    @PostMapping("/waiting")
    public String createWaiting(@ModelAttribute WaitingRequest waitingRequest, RedirectAttributes redirectAttributes) {
        log.debug("=== create Waiting ===");
        Long waitingId = waitingCustomerService.createWaiting(waitingRequest.getStoreId(),
                waitingRequest.getPeopleCount());
        redirectAttributes.addAttribute("waitingId", waitingId);

        return "redirect:/my-seat-dang/waiting/{waitingId}/awaiting/detail";
    }

    @GetMapping("/waiting")
    public String showWaiting(@RequestParam(defaultValue = "0") int status, Model model) {
        List<WaitingInfoDto> waitings = waitingCustomerService.showWaiting(status);
        model.addAttribute("waitings", waitings);

        return "customer/waiting/waiting";
    }

    // TODO : 취소 후 url에 접속 못하게 막기(if문 상태처리)
    @GetMapping("/waiting/{waitingId}/awaiting/detail")
    public String showAwaitingWaitingDetail(@PathVariable Long waitingId, Model model) {
        Waiting waiting = waitingRepository.findById(waitingId).get();
        Store store = storeRepository.findByStoreId(waiting.getStoreId());
        model.addAttribute("awaitingWaitingResponse", AwaitingWaitingResponse.create(waiting, store));

        return "customer/waiting/awaiting-waiting-detail";
    }

    @PostMapping("/waiting/{waitingId}/awaiting/detail")
    public String cancelWaiting(@PathVariable Long waitingId, RedirectAttributes redirectAttributes) {
        int result = waitingRepository.cancelWaitingByCustomer(waitingId);
        if (result == 1) {
            log.info("=== 웨이팅 고객 취소 ===");
        } else {
            log.error("== 웨이팅 고객 취소 오류 ===");
        }

        redirectAttributes.addAttribute("waitingId", waitingId);

        return "redirect:/my-seat-dang/waiting/{waitingId}/canceled/detail";
    }

    @GetMapping("/waiting/{waitingId}/canceled/detail")
    public String showCanceledWaitingDetail(@PathVariable Long waitingId, Model model) {
        Waiting waiting = waitingRepository.findById(waitingId).get();
        Store store = storeRepository.findByStoreId(waiting.getStoreId());
        model.addAttribute("canceledWaitingResponse", CanceledWaitingResponse.create(waiting, store));

        return "customer/waiting/canceled-waiting-detail";
    }


    @GetMapping("/waiting/{waitingId}/visited/detail")
    public String showVisitedWaitingDetail(@PathVariable Long waitingId, Model model) {
        Waiting waiting = waitingRepository.findById(waitingId).get();
        Store store = storeRepository.findByStoreId(waiting.getStoreId());
        model.addAttribute("visitedWaitingResponse", VisitedWaitingResponse.create(waiting, store));

        return "customer/waiting/visited-waiting-detail";
    }
}
