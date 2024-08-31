package com.matdang.seatdang.application.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainController {

    @GetMapping("/customer")
    public String customerMain(){
        return "customer/main";
    }

    @GetMapping("/store-owner/main")
    public String storeOwnerMain(){
        return "storeowner/main";
    }

    @GetMapping("/admin/main")
    public String adminMain() {
        return "admin/main";
    }

}
