package com.matdang.seatdang.application.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String customerMain(){
        return "/customer/main";
    }
}
