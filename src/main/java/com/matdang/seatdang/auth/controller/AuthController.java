package com.matdang.seatdang.auth.controller;

import com.matdang.seatdang.auth.dto.CustomOAuth2User;
import com.matdang.seatdang.auth.principal.CustomerUserDetails;
import com.matdang.seatdang.auth.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

        // 현재 인증된 사용자를 가져옵니다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {

            if (authentication.getPrincipal() instanceof CustomerUserDetails) {
                // 세션 로그인 사용자
                CustomerUserDetails userDetails = (CustomerUserDetails) authentication.getPrincipal();

                //현재 지금 사용자 이름 , 사용자 닉네임, 프로필이미지만 테스트로 불러옴 원래는 계정이 가지고있는정보 모두 불러와야됨(수정하기)

                String memberName = userDetails.getRealName(); // 사용자 이름
                String memberNickName = userDetails.getCustomerNickName(); // 사용자 닉네임
                String customerProfileImage = userDetails.getCustomerProfileImage(); // 프로필 이미지

                model.addAttribute("memberName", memberName);
                model.addAttribute("memberNickName", memberNickName);
                model.addAttribute("customerProfileImage", customerProfileImage);

            } else if (authentication.getPrincipal() instanceof CustomOAuth2User) {
                // OAuth 로그인 사용자
                CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();

                String memberName = oauthUser.getName(); // 사용자 이름 (OAuth2User의 getName() 메서드 사용)
                String memberNickName = oauthUser.getEmail(); // OAuth2에서 닉네임이 없으면 이메일을 사용 ( 그냥 테스트로한거임 바꿀예정)
                String customerProfileImage = ""; // OAuth2에서는 이미지없음.. 초기화하거나 기본 이미지 사용

                model.addAttribute("memberName", memberName);
                model.addAttribute("memberNickName", memberNickName);
                model.addAttribute("customerProfileImage", customerProfileImage);
            }

        } else {
            return "redirect:/login";
        }

        return "customer/mypage/mainmypage";
    }
}
