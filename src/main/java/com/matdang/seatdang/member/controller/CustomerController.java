package com.matdang.seatdang.member.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/")
public class CustomerController {

    //나경작업시작

    //사용자회원가입 페이지 바로가기
    @GetMapping("/signup")
    public String account() {
        return "customer/member/signup";
    }

    //나경작업끝
}
