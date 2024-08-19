package com.matdang.seatdang.reservation.controller;

import com.matdang.seatdang.auth.principal.MemberUserDetails;
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
@RequestMapping("/myseatdang")
@RequiredArgsConstructor
public class CustomerMainController {

    @Autowired
    private ReservationService reservationService;


    @GetMapping("/detail")
    public String detail() {
        return "customer/shop/detail";
    }


    @GetMapping("/mypage")
    public String mypage(Model model) {
            // 고객 ID가 제공되면 해당 고객의 예약 목록을 가져와서 모델에 추가합니다.
        Long customerId = ((MemberUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        List<ResponseDto> reservations = reservationService.getReservationsByCustomerId(customerId);
        model.addAttribute("reservations", reservations);
        return "customer/mypage/mypage";
    }
}
