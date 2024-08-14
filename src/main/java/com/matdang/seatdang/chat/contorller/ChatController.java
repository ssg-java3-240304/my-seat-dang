package com.matdang.seatdang.chat.contorller;

import com.matdang.seatdang.chat.chatconfig.ChatConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/")
@RequiredArgsConstructor
public class ChatController {
    @Autowired
    private final ChatConfig chatConfig;
    @GetMapping("/popup")
    public String popup(Model model) {
        model.addAttribute("chatAceessUrl",chatConfig.getServerUrl());
        System.out.println(chatConfig.getServerUrl());
        return "customer/mypage/popup";
    }
}
