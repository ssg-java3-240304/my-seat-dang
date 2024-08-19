package com.matdang.seatdang.reservation.controller;

import com.matdang.seatdang.auth.principal.StoreOwnerUserDetails;
import com.matdang.seatdang.reservation.dto.ResponseDto;
import com.matdang.seatdang.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@Slf4j
@RequestMapping("/store")
@RequiredArgsConstructor
public class StoreOwnerController {

    @Autowired
    private ReservationService reservationService;



    @GetMapping
    public String storeownerpage(Model model) {
        // 고객 ID가 제공되면 해당 고객의 예약 목록을 가져와서 모델에 추가합니다.
        Long storeOwnerId = ((StoreOwnerUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        List<ResponseDto> reservations = reservationService.getReservationsByStoreOwnerId(storeOwnerId);
        model.addAttribute("reservations", reservations);

        log.debug("reservation = {}",reservations);

        return "store/mypage/storeownerpage";

    }
}
