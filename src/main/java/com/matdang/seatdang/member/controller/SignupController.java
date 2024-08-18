package com.matdang.seatdang.member.controller;

import com.matdang.seatdang.member.dto.CustomerSignupDto;
import com.matdang.seatdang.member.service.CustomerSignupService;
import com.matdang.seatdang.object_storage.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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


    // 닉네임 중복 확인
    @PostMapping("/check-nickname")
    @ResponseBody
    public boolean checkNickname(@RequestParam String customerNickName) {
        return customerSignupService.isNicknameDuplicate(customerNickName);
    }

    //이메일 중복 확인
    @PostMapping("/check-email")
    @ResponseBody
    public boolean checkMemberEmail(@RequestParam String memberEmail) {
        log.debug("입력한 이메일 : {}", memberEmail);
        return customerSignupService.isEmailDuplicate(memberEmail);
    }

    // 사용자 회원가입 값 요청
    @PostMapping("/signupProc")
    public String joinProcess(@ModelAttribute CustomerSignupDto customerSignupDto,@RequestParam("customerProfileImage") MultipartFile customerProfileImage){

        customerSignupService.signupProcess(customerSignupDto,customerProfileImage);

        return "redirect:/login"; // 이 경로로 오면 로그인 바로 할 수 있게
    }













}
