package com.matdang.seatdang.auth.controller;

import com.matdang.seatdang.auth.dto.CustomOAuth2User;
import com.matdang.seatdang.auth.principal.CustomerUserDetails;
import com.matdang.seatdang.auth.service.AuthService;
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
        CustomerUserDetails userDetails = authService.getAuthenticatedUserDetails();

        if (userDetails != null) {
            //현재 지금 사용자 이름 , 사용자 닉네임, 프로필이미지만 테스트로 불러옴 원래는 계정이 가지고있는정보 모두 불러와야됨(수정하기)
            //Customer : 이메일,실명,핸드폰번호,성별,생일,닉네임,프로필이미지 가져와야됨

            model.addAttribute("memberName", userDetails.getRealName()); // 사용자 이름
            model.addAttribute("memberNickName", userDetails.getCustomerNickName()); // 사용자 닉네임
            model.addAttribute("customerProfileImage", userDetails.getCustomerProfileImage()); // 프로필 이미지
            model.addAttribute("memberEmail",userDetails.getUsername()); // 아이디
            model.addAttribute("memberPhone",userDetails.getMemberPhone()); // 휴대폰 번호
            model.addAttribute("customerGender",userDetails.getCustomerGender()); // 성별
            model.addAttribute("customerBirthday",userDetails.getCustomerBirthday()); // 생일


        } else {
            // OAuth 로그인 사용자 정보를 가져옴
            CustomOAuth2User oAuth2User = authService.getAuthenticatedOAuth2UserDetails();

            if (oAuth2User != null) {

//                String memberName = oAuth2User.getName(); // 사용자 이름 (OAuth2User의 getName() 메서드 사용)
//                String memberNickName = oAuth2User.getEmail(); // OAuth2에서 닉네임이 없으면 이메일을 사용 ( 그냥 테스트로한거임 바꿀예정)
//                String customerProfileImage = ""; // OAuth2에서는 이미지없음.. 초기화하거나 기본 이미지 사용

                model.addAttribute("memberName", oAuth2User.getName()); // 사용자 이름
                model.addAttribute("memberNickName", "만들자닉네임"); // 사용자 닉네임
                model.addAttribute("customerProfileImage", "메서드만들어 기본이미지 올리고 링크걸자 업성"); // 프로필 이미지
                model.addAttribute("memberEmail", oAuth2User.getEmail()); // 아이디 == 이메일
                model.addAttribute("memberPhone","휴대폰번호가져오는거안만들었네"); // 휴대폰 번호
                model.addAttribute("customerGender","성별가져오는것도만들자"); // 성별
                model.addAttribute("customerBirthday","생일도 만들자"); // 생일


            } else {
                // 인증 되지 않은 경우 로그인 페이지로 리다이렉트
                return "redirect:/login";
            }
        }
        return "customer/mypage/mainmypage";
    }
}
