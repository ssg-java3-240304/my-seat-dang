package com.matdang.seatdang.ai.controller;


import com.matdang.seatdang.ai.service.AIService;
import com.matdang.seatdang.auth.service.AuthService;
import com.matdang.seatdang.member.service.CustomerService;
import com.matdang.seatdang.object_storage.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
@Slf4j
@RequiredArgsConstructor
public class AIController {

    private final AIService aiService;

// 사용할때 주석 푸세요

    @GetMapping("/ai-image")
    public String getImage(Model model)
            throws IOException, InterruptedException {
        String prompt = aiService.
                generatePictureV2("케이크 시안을 원하는데 동그란시트 겨울왕국 올라프케이크 생성해줘");
        model.addAttribute("url", prompt);
        return "test";
    }
}