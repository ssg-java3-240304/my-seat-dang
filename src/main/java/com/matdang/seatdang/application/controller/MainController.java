package com.matdang.seatdang.application.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/my-seat-dang")
    public String customerMain(){
        return "customer/main";
    }

    @GetMapping("/store-owner/main")
    public String storeOwnerMain(){
        return "storeowner/main";
    }
}
