package com.matdang.seatdang.customer.main.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/")
public class CustomerController {
    @GetMapping("/")
    public String index() {
        return "customer/main/main";
    }

    @GetMapping("/detail")
    public String detail() {
        return "customer/shop/detail";
    }

    @GetMapping("/mypage")
    public String mypage(){
        return "customer/mypage/mypage";
    }

    @GetMapping("/popup")
    public String popup() {
        return "/customer/mypage/popup";
    }
}
