package com.matdang.seatdang.reservation.controller;

import com.matdang.seatdang.auth.principal.StoreOwnerUserDetails;
import com.matdang.seatdang.chat.chatconfig.ChatConfig;
import com.matdang.seatdang.reservation.dto.ReservationResponseDto;
import com.matdang.seatdang.reservation.service.ReservationQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@Slf4j
@RequestMapping("/store-owner/reservation")
@RequiredArgsConstructor
public class ReservationStoreOwnerController {
    private final ReservationQueryService reservationQueryService;
    private final ChatConfig chatConfig;  // ChatConfig를 의존성 주입으로 받음

    @GetMapping("/list")
    public String storeChatPage(Model model) {

        // SecurityContext에서 상점 ID를 가져옴
        Long storeOwnerId = ((StoreOwnerUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        String chatUrl = chatConfig.getServerUrl();


        // 예약 목록 가져오기
        List<ReservationResponseDto> reservations = reservationQueryService.getReservationsByStoreOwnerId(storeOwnerId);
        model.addAttribute("reservations", reservations);
        model.addAttribute("chatAccessUrl", chatUrl);
        log.debug("reservation = {}", reservations);
        return "storeowner/reservation/store-reservationlist";
    }
}