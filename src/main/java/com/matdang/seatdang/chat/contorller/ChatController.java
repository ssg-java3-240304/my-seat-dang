package com.matdang.seatdang.chat.contorller;

import com.matdang.seatdang.auth.principal.MemberUserDetails;
import com.matdang.seatdang.chat.chatconfig.ChatConfig;
import com.matdang.seatdang.customer.main.model.ResponseDto;
import com.matdang.seatdang.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@Slf4j
@RequestMapping("/")
@RequiredArgsConstructor
public class ChatController {
        @GetMapping("/popup")
        public String popup(@RequestParam("reservationId") Long reservationId,
                                @RequestParam("storeName") String storeName,
                                @RequestParam("customerName") String customerName,
                                @RequestParam("chatUrl") String chatUrl,
                                Model model) {
            // 데이터가 URL 파라미터로 넘어오기 때문에 별도의 데이터 처리 로직이 필요하지 않을 수 있음
            model.addAttribute("reservationId", reservationId);
            model.addAttribute("storeName", storeName);
            model.addAttribute("customerName", customerName);
            model.addAttribute("chatAccessUrl", chatUrl);

            log.info("Received parameters - reservationId: {}, storeName: {}, customerName: {}, chatUrl: {}",
                    reservationId, storeName, customerName, chatUrl);

            return "customer/mypage/popup";
        }
}
