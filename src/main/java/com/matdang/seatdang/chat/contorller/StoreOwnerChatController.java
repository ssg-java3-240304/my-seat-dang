package com.matdang.seatdang.chat.contorller;

import com.matdang.seatdang.auth.principal.StoreOwnerUserDetails;
import com.matdang.seatdang.reservation.dto.ResponseDto;
import com.matdang.seatdang.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@Slf4j
@RequestMapping("/store")
@RequiredArgsConstructor
public class StoreOwnerChatController {
    @Autowired
    private final ReservationService reservationService;
        @GetMapping("/streservedpage")
        public String storeChatPage(Model model,  @RequestParam(required = false)String chatUrl) {

            // SecurityContext에서 고객 ID를 가져옴
            Long storeOwnerId = ((StoreOwnerUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
            if (chatUrl == null) {
                chatUrl = "http://localhost:8080";  // 또는 기본 URL 설정
            }

            // 예약 목록 가져오기
            List<ResponseDto> reservations = reservationService.getReservationsByStoreOwnerId(storeOwnerId);
            model.addAttribute("chatAccessUrl", chatUrl);
            model.addAttribute("reservations", reservations);

            log.debug("reservation = {}",reservations);
            return "storeowner/mypage/streservedpage";
        }
}
