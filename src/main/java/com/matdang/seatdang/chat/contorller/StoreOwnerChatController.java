package com.matdang.seatdang.chat.contorller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@Slf4j
@RequestMapping("/store")
@RequiredArgsConstructor
public class StoreOwnerChatController {
        @GetMapping("/storeownerchat")
        public String storeChatPage(@RequestParam("reservationId") Long reservationId,
                                @RequestParam("storeName") String storeName,
                                @RequestParam("storeOwnerName") String storeOwnerName,
                                @RequestParam("chatUrl") String chatUrl,
                                Model model) {
            // 데이터가 URL 파라미터로 넘어오기 때문에 별도의 데이터 처리 로직이 필요하지 않을 수 있음
            model.addAttribute("reservationId", reservationId);
            model.addAttribute("storeName", storeName);
            model.addAttribute("storeOwnerName", storeOwnerName);
            model.addAttribute("chatAccessUrl", chatUrl);

            log.info("Received parameters - reservationId: {}, storeName: {}, storeOwnerName: {}, chatUrl: {}",
                    reservationId, storeName, storeOwnerName, chatUrl);

            return "store/mypage/storeownerchat";
        }
}
