package com.matdang.seatdang.waiting.controller;

import com.matdang.seatdang.auth.service.AuthService;
import com.matdang.seatdang.store.entity.Store;
import com.matdang.seatdang.store.repository.StoreRepository;
import com.matdang.seatdang.waiting.controller.dto.*;
import com.matdang.seatdang.waiting.dto.WaitingId;
import com.matdang.seatdang.waiting.repository.WaitingRepository;
import com.matdang.seatdang.waiting.repository.WaitingStorageRepository;
import com.matdang.seatdang.waiting.repository.query.dto.WaitingInfoDto;
import com.matdang.seatdang.waiting.service.WaitingCustomerService;
import com.matdang.seatdang.waiting.service.facade.RedissonLockWaitingCustomerFacade;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Slf4j
@Controller
@RequestMapping("/my-seat-dang")
@RequiredArgsConstructor
public class WaitingCustomerController {
    private final RedissonLockWaitingCustomerFacade redissonLockWaitingCustomerFacade;
    private final WaitingCustomerService waitingCustomerService;
    private final WaitingStorageRepository waitingStorageRepository;
    private final WaitingRepository waitingRepository;
    private final StoreRepository storeRepository;
    private final AuthService authService;

    @GetMapping("/test-store")
    public String showStore(@RequestParam(defaultValue = "2") Long storeId,
                            Model model) {
        Long memberId = authService.getAuthenticatedMember().getMemberId();
        boolean isWaitingExists = waitingCustomerService.isWaitingExists(storeId);

        model.addAttribute("storeId", storeId);
        model.addAttribute("isWaitingExists", isWaitingExists);

        return "customer/waiting/test-store";
    }

    /**
     * TODO : 삭제 필요
     * defaultValue는 test 용도임
     * <p>
     * TODO : URL 변경
     */
    // TODO : URL 직접 접근 막기 - 개선필요
    @GetMapping("/waiting/{storeId}")
    public String readyWaiting(@PathVariable Long storeId, Model model, HttpServletRequest request,
                               RedirectAttributes redirectAttributes) {
        if (waitingCustomerService.isWaitingExists(storeId)) {
            redirectAttributes.addFlashAttribute("status", true);
            return "redirect:/my-seat-dang/test-store";
        }

        String referer = request.getHeader("Referer");
        // 유효한 referer URL인지 확인 (예: "https://example.com/somepage")
        if (referer == null || !referer.startsWith("http://localhost:8080/my-seat-dang/test-store")) {
            return "error/403";
        }

        Store store = storeRepository.findByStoreId(storeId);

        model.addAttribute("waitingTeam", waitingRepository.countWaitingByStoreIdAndWaitingStatus(storeId));
        model.addAttribute("readyWaitingResponse", ReadyWaitingResponse.create(store));

        return "customer/waiting/registration";
    }

    @PostMapping("/waiting")
    public String createWaiting(@ModelAttribute WaitingRequest waitingRequest, RedirectAttributes redirectAttributes) {
        log.debug("=== create Waiting ===");
        log.debug("=== create Waiting === {}", LocalDateTime.now());
        WaitingId waitingId = redissonLockWaitingCustomerFacade.createWaiting(waitingRequest.getStoreId(),
                waitingRequest.getPeopleCount());
        redirectAttributes.addAttribute("waitingNumber", waitingId.getWaitingNumber());
        redirectAttributes.addAttribute("storeId", waitingId.getStoreId());

        return "redirect:/my-seat-dang/waiting/{waitingNumber}/awaiting/detail";
    }

    @GetMapping("/waiting")
    public String showWaiting(
            @RequestParam(defaultValue = "today") String when,
            @RequestParam(defaultValue = "0") int status,
            @RequestParam(defaultValue = "0") int page,
            Model model) {
        Page<WaitingInfoDto> waitings = null;
        if (when.equals("today")) {
            waitings = waitingCustomerService.showTodayWaiting(status, page);
        } else if (when.equals("history")) {
            waitings = waitingCustomerService.showHistoryWaiting(status, page);
        }
        System.out.println("waitings = " + waitings.getContent());
        System.out.println("waitings = " + waitings.getTotalElements());
        model.addAttribute("when", when);
        model.addAttribute("status", status);
        model.addAttribute("waitings", waitings.getContent());
        model.addAttribute("currentPage", waitings.getNumber());
        model.addAttribute("totalPages", waitings.getTotalPages());

        return "customer/waiting/waiting";
    }

    // TODO : 취소 후 url에 접속 못하게 막기(if문 상태처리)

    @PostMapping("/waiting/{waitingNumber}/awaiting/detail")
    public String cancelWaiting(
            @PathVariable Long waitingNumber, @RequestParam Long storeId, RedirectAttributes redirectAttributes) {
        log.debug("=== cancel Waiting === {}", LocalDateTime.now());

        redissonLockWaitingCustomerFacade.cancelWaitingByCustomer(waitingNumber, storeId);
        log.info("=== 웨이팅 고객 취소 ===");
//        if (result > 0) {
//            log.info("=== 웨이팅 고객 취소 ===");
//        } else {
//            log.error("== 웨이팅 고객 취소 오류 ===");
//        }

        redirectAttributes.addAttribute("waitingNumber", waitingNumber);
        redirectAttributes.addAttribute("storeId", storeId);

        return "redirect:/my-seat-dang/waiting/{waitingNumber}/canceled/detail";
    }

    @GetMapping("/waiting/{waitingNumber}/{status}/detail")
    public String showWaitingDetail(@PathVariable Long waitingNumber,
                                    @PathVariable String status,
                                    @RequestParam Long storeId,
                                    @RequestParam(defaultValue = "today") String when,
                                    Model model,
                                    HttpServletRequest request,
                                    RedirectAttributes redirectAttributes) {
        // Referer 검증 (awaiting 상태일 때만)
        if (waitingCustomerService.isIncorrectWaitingStatus(storeId, waitingNumber, status)) {
            redirectAttributes.addFlashAttribute("isIncorrectWaitingStatus", true);
            System.out.println("= fail =");

            return "redirect:/my-seat-dang/waiting";
        }

        System.out.println("= pass =");

        if ("awaiting".equals(status)) {
            String referer = request.getHeader("Referer");
            if (referer == null || !referer.startsWith("http://localhost:8080/my-seat-dang/waiting")) {
                return "error/403";
            }
        }

        Object response = getWaitingDetailResponse(new WaitingId(storeId, waitingNumber), status, when);
        model.addAttribute("waitingDetailResponse", response);
        model.addAttribute("when", when);

        log.debug("=== create finish Waiting === {}", LocalDateTime.now());
        log.debug("=== cancel finish Waiting === {}", LocalDateTime.now());

        // 상태에 따라 뷰 이름을 반환
        return getViewName(status);
    }

    private Object getWaitingDetailResponse(WaitingId waitingId, String status, String when) {
        Object waiting = findWaitingEntity(waitingId, when);
        Store store = storeRepository.findByStoreId(waitingId.getStoreId());

        switch (status) {
            case "canceled":
                return CanceledWaitingResponse.create(waiting, store);
            case "visited":
                return VisitedWaitingResponse.create(waiting, store);
            case "awaiting":
                return AwaitingWaitingResponse.create(waiting, store);
            default:
                throw new IllegalArgumentException("Invalid status: " + status);
        }
    }

    private Object findWaitingEntity(WaitingId waitingId, String when) {
        if (when.equals("today")) {
            return waitingCustomerService.findById(waitingId);
        } else if (when.equals("history")) {
            return waitingStorageRepository.findByStoreIdAndWaitingNumber(waitingId.getStoreId(),
                    waitingId.getWaitingNumber());
        }

        return null;
    }


    // 상태에 따라 뷰 이름을 반환하는 메서드
    private String getViewName(String status) {
        switch (status) {
            case "canceled":
                return "customer/waiting/canceled-waiting-detail";
            case "visited":
                return "customer/waiting/visited-waiting-detail";
            case "awaiting":
                return "customer/waiting/awaiting-waiting-detail";
            default:
                throw new IllegalArgumentException("Invalid status: " + status);
        }
    }
}
