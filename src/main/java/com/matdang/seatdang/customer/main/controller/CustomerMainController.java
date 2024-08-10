package com.matdang.seatdang.customer.main.controller;

import com.matdang.seatdang.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/")
@RequiredArgsConstructor
public class CustomerMainController {
    private final ChatService chatService;
    @GetMapping("/")
    public String index() {
        return "customer/main/main";
    }

    @GetMapping("/detail")
    public String detail() {
        return "customer/shop/detail";
    }


    @GetMapping("/mypage")
    public String mypage() {
        return "customer/mypage/mypage";
    }

    @GetMapping("/popup")
    public String popup(Model model) {
        model.addAttribute("chatAceessUrl",chatService.getServerUrl());
        System.out.println(chatService.getServerUrl());
        return "customer/mypage/popup";
    }
}
