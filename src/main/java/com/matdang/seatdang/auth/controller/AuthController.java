package com.matdang.seatdang.auth.controller;

import com.matdang.seatdang.auth.dto.CustomOAuth2User;
import com.matdang.seatdang.auth.principal.CustomerUserDetails;
import com.matdang.seatdang.auth.service.AuthService;
import com.matdang.seatdang.member.entitiy.Customer;
import com.matdang.seatdang.member.entitiy.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
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

//    @GetMapping("logout")
//    public


    @GetMapping("/mainmypage")
    public String mainMyPage(Model model) {

        // 현재 인증된 사용자를 가져옵니다. ( 일반세션)
        Member member = authService.getAuthenticatedMember();

        if (member != null) {
            // Member 정보를 모델에 추가
            model.addAttribute("memberName", member.getMemberName()); // 사용자 이름
            model.addAttribute("memberNickName", member instanceof Customer ? ((Customer) member).getCustomerNickName() : ""); // 닉네임 (Customer일 경우)
            model.addAttribute("customerProfileImage", member instanceof Customer ? ((Customer) member).getCustomerProfileImage() : ""); // 프로필 이미지
            model.addAttribute("memberEmail", member.getMemberEmail()); // 이메일
            model.addAttribute("memberPhone", member.getMemberPhone()); // 휴대폰 번호
            model.addAttribute("customerGender", member instanceof Customer ? ((Customer) member).getCustomerGender() : ""); // 성별 (Customer일 경우)
            model.addAttribute("customerBirthday", member instanceof Customer ? ((Customer) member).getCustomerBirthday() : ""); // 생일 (Customer일 경우)
            model.addAttribute("imageGenLeft", member instanceof Customer ? ((Customer) member).getImageGenLeft() : ""); // ai생성횟수 (Customer일 경우)
        } else {
            // 인증되지 않은 경우 로그인 페이지로 리다이렉트
            return "redirect:/login";
        }
        return "customer/mypage/mainmypage";
    }
}
