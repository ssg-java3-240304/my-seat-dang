package com.matdang.seatdang.auth.controller;

import com.matdang.seatdang.member.dto.CustomerDto;
import com.matdang.seatdang.auth.service.AuthService;
import com.matdang.seatdang.member.service.CustomerService;
import com.matdang.seatdang.member.entity.Customer;
import com.matdang.seatdang.member.entity.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 인증(Authentication)과 관련된 모든 기능
 *
 *
 */


@Controller
@Slf4j
@RequestMapping("/")
public class AuthController {

    private final AuthService authService;
    private final CustomerService customerService;
    public AuthController(AuthService authService,CustomerService customerService) {
        this.authService = authService;
        this.customerService = customerService;
    }


    //로그인 페이지 바로가기(셋다 공통으로 감)
    @GetMapping("/login")
    public String login() {
        return "customer/member/login";
    }

//    @GetMapping("logout")
//    public
}
