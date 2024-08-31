package com.matdang.seatdang.reservation.controller;

import com.matdang.seatdang.auth.principal.MemberUserDetails;
import com.matdang.seatdang.auth.principal.StoreOwnerUserDetails;
import com.matdang.seatdang.chat.chatconfig.ChatConfig;
import com.matdang.seatdang.common.storeEnum.StoreType;
import com.matdang.seatdang.member.entity.MemberRole;
import com.matdang.seatdang.reservation.dto.ReservationCancelRequestDto;
import com.matdang.seatdang.reservation.dto.ReservationResponseDto;
import com.matdang.seatdang.reservation.service.ReservationCommandService;
import com.matdang.seatdang.reservation.service.ReservationQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/store-owner/reservation")
@RequiredArgsConstructor
public class ReservationStoreOwnerController {
    private final ReservationQueryService reservationQueryService;
    private final ReservationCommandService reservationCommandService;
    private final ChatConfig chatConfig;  // ChatConfig를 의존성 주입으로 받음

    @GetMapping("/list")
    public String storeChatPage(Model model) {

        // SecurityContext에서 상점 ID를 가져옴
        Long storeOwnerId = ((StoreOwnerUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        String chatUrl = chatConfig.getServerUrl();
        StoreType storeType = ((StoreOwnerUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getStore().getStoreType();

        // 예약 목록 가져오기
        List<ReservationResponseDto> reservations = reservationQueryService.getReservationsByStoreOwnerId(storeOwnerId);

        model.addAttribute("reservations", reservations);
        model.addAttribute("chatAccessUrl", chatUrl);
        model.addAttribute("storeType", storeType);

        log.debug("reservation = {}", reservations);
        return "storeowner/reservation/store-reservationlist";
    }

    @PostMapping("/cancel")
    public String cancel(@ModelAttribute ReservationCancelRequestDto cancelRequestDto){
        log.debug("cancelRequest Test Dto: {}", cancelRequestDto.toString());
        MemberRole role = ((MemberUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMemberRole();
        cancelRequestDto.setCancelledAt(LocalDateTime.now());
        cancelRequestDto.setCancelledBy(role);
        reservationCommandService.cancelReservation(cancelRequestDto);
        return "redirect:/store-owner/reservation/list";
    }

    @PostMapping("/complete")
    public String cancel(@RequestParam Long reservationId){
        log.debug("completeRequest Test reservation Id: {}", reservationId);
        reservationCommandService.updateStatusToCompleted(reservationId);
        return "redirect:/store-owner/reservation/list";
    }

    @GetMapping("/detail")
    public String detail(@RequestParam Long reservationId, Model model){
        log.debug("detailRequest Test reservation Id: {}", reservationId);
        model.addAttribute("reservation", reservationQueryService.getReservationById(reservationId));
        return "storeowner/reservation/store-reservation-detail";
    }
}