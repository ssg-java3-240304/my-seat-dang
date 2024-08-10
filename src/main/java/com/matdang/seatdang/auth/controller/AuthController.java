package com.matdang.seatdang.auth.controller;

import com.matdang.seatdang.auth.principal.CustomerUserDetails;
import com.matdang.seatdang.auth.service.AuthService;
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

    public AuthController(AuthService authService) {
        this.authService = authService; //
    }



    //로그인 페이지 바로가기(셋다 공통으로 감)
    @GetMapping("/login")
    public String login() {
        return "customer/member/login";
    }

    @GetMapping("/mainmypage")
    public String mainMyPage(Model model) {

        CustomerUserDetails userDetails = authService.getAuthenticatedUserDetails();
        if (userDetails != null) {
//             사용자 이름,닉네임,프로필 이미지 가져오기
            String memberName = userDetails.getRealName(); // 사용자 이름
            String memberNickName = userDetails.getCustomerNickName(); // 사용자 닉네임
            String customerProfileImage = userDetails.getCustomerProfileImage(); // 프로필 이미지

            // 모델에 데이터 추가
            model.addAttribute("memberName", memberName);
            model.addAttribute("memberNickName", memberNickName);
            model.addAttribute("customerProfileImage", customerProfileImage);

        } else {
            return "redirect:/login";
        }


        return "customer/mypage/mainmypage";

    }

}
