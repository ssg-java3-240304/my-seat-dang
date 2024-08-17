package com.matdang.seatdang.auth.controller;

import com.matdang.seatdang.auth.dto.CustomerDto;
import com.matdang.seatdang.auth.service.AuthService;
import com.matdang.seatdang.auth.service.CustomerService;
import com.matdang.seatdang.member.entity.Customer;
import com.matdang.seatdang.member.entity.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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


    @GetMapping("/mainmypage")
    public String mainMyPage(Model model) {

        // 현재 인증된 사용자를 가져옵니다. ( 일반세션)
        Member member = authService.getAuthenticatedMember();

        if (member != null) {

            Customer customer = (Customer) member;

            // Member 정보를 모델에 추가
            CustomerDto customerDto = customerService.convertToDto(customer);

            // DTO를 모델에 추가
            model.addAttribute("customer", customerDto);
        } else {
            // 인증되지 않은 경우 로그인 페이지로 리다이렉트
            return "redirect:/login";
        }
        return "customer/mypage/mainmypage";
    }
}
