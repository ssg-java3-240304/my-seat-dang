package com.matdang.seatdang.dashboard.controller;

import com.matdang.seatdang.auth.principal.StoreOwnerUserDetails;
import com.matdang.seatdang.dashboard.dto.WaitingDashboardResponseDto;
import com.matdang.seatdang.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequestMapping("/store-owner/mypage")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

//    @GetMapping("/dashboard")
//    public ResponseEntity<Map<LocalDate, Long>> getWeeklyWaitingCnt() {
//        StoreOwnerUserDetails userDetails = (StoreOwnerUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        Long storeId = userDetails.getStore().getStoreId();
//        log.debug("storeId = {}", storeId);
//        Map<LocalDate, Long> weeklyWaitingCnt = dashboardService.getWeeklyWaitingCnt(storeId);
//        log.debug("weeklyWaitingCnt = {}", weeklyWaitingCnt);
//        return ResponseEntity.ok(weeklyWaitingCnt);
//    }

    @GetMapping("/dashboard")
    public String getWeeklyWaitingCnt(Model model){
        StoreOwnerUserDetails userDetails = (StoreOwnerUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long storeId = userDetails.getStore().getStoreId();
        Map<LocalDate, Long> weeklyWaitingCnt = dashboardService.getWeeklyWaitingCnt(storeId);
        log.debug("storeId = {}", storeId);
        model.addAttribute("weeklyWaitingCnt", weeklyWaitingCnt);
        log.debug("weeklyWaitingCnt = {}", weeklyWaitingCnt);
        return "storeowner/mypage/dashboard";
    }

//    @GetMapping("/dashboard")
//    public Map<String, Object> getWeeklyWaitingCnt(){
//        StoreOwnerUserDetails userDetails = (StoreOwnerUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        Long storeId = userDetails.getStore().getStoreId();
//        log.debug("storeId = {}", storeId);
//        long weeklyWaitingCnt = dashboardService.getWeeklyWaitingCnt(storeId);
//        return putResponse(weeklyWaitingCnt);
//    }
//
//    private Map<String, Object> putResponse(long weeklyWaitingCnt) {
//        Map<String, Object> response = new HashMap<>();
//        response.put("putResponse", weeklyWaitingCnt);
//        log.debug("weeklyWaitingCnt = {}", weeklyWaitingCnt);
//        return response;
//    }

//    @GetMapping("/dashboard")
//    public String getWeeklyWaitingCnt(Model model){
//        StoreOwnerUserDetails userDetails = (StoreOwnerUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        Long storeId = userDetails.getStore().getStoreId();
//        // 일별 웨이팅 건수를 조회
//        Map<LocalDate, Long> weeklyWaitingCnt = dashboardService.getWeeklyWaitingCnt(storeId);
//        log.debug("weeklyWaitingCnt = {}", weeklyWaitingCnt);
//
//        // 결과를 모델에 추가
//        model.addAttribute("weeklyWaitingCnt", weeklyWaitingCnt);
//        return "storeowner/mypage/dashboard";
//    }
}
