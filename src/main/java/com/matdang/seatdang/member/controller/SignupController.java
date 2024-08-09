package com.matdang.seatdang.member.controller;

import com.matdang.seatdang.member.dto.CustomerSignupDto;
import com.matdang.seatdang.member.service.CustomerSignupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
@RequestMapping("/")
public class SignupController {

    private final CustomerSignupService customerSignupService; // final 키워드를 사용하여 필드를 상수로 만듦
    @Autowired
    public SignupController(CustomerSignupService customerSignupService) {
        this.customerSignupService = customerSignupService; // 생성자 주입 방식
    }


    //사용자회원가입 페이지 바로가기
    @GetMapping("/signup")
    public String join() {
        return "customer/member/signup";
    }

    // 사용자 회원가입 값 요청
    @PostMapping("/signupProc")
    public String joinProcess(@ModelAttribute CustomerSignupDto customerSignupDto){

        System.out.println("컨트롤러");

        customerSignupService.joinProcess(customerSignupDto);

        return "redirect:/login"; // 이 경로로 오면 로그인 바로 할 수 있게
    }











}
