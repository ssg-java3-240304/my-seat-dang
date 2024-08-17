package com.matdang.seatdang.auth.service;

import com.matdang.seatdang.auth.dto.CustomOAuth2User;
import com.matdang.seatdang.auth.dto.NaverResponse;
import com.matdang.seatdang.auth.dto.OAuth2Response;
import com.matdang.seatdang.member.entity.*;
import com.matdang.seatdang.member.repository.MemberRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public CustomOAuth2UserService(MemberRepository memberRepository,BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.memberRepository = memberRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    // 권한에서 MemberRole을 추출
    private MemberRole extractRoleFromAuthorities(Collection<? extends GrantedAuthority> authorities) {
        if (authorities != null && !authorities.isEmpty()) {
            String roleName = authorities.iterator().next().getAuthority(); // 첫 번째 권한만 사용
            return MemberRole.valueOf(roleName); // MemberRole로 변환
        }
        return MemberRole.ROLE_CUSTOMER; // 기본값
    }



    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User.getAttributes());

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response;

        // 기본 프로필 이미지 URL 설정
        String basicProfileImageUrl = "https://kr.object.ncloudstorage.com/myseatdang-bucket/member/3539241b-bf4c-474b-abdd-3d32f9841d9c.jpg";




        // 현재 네이버 밖에 안했지만 추후 다른 OAuth를 사용할 경우 여기다가 추가하면된다.

        if (registrationId.equals("naver")) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes()); // 네이버 dto로 받음

        } else {
            return null;
        }


        //1. OAuth 식별자로 사용자 조회 (OAuth로 회원가입 한적 있는지?)
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(oAuth2Response);
        String oauthIdentifier = customOAuth2User.getUsername();
        Customer customerByOauth = memberRepository.findByOauthIdentifier(oauthIdentifier); // ouath 식별 아이디 oauthIdentifier


        if (customerByOauth != null) {
            // OAuth 식별자로 이미 가입된 사용자라면 해당 사용자 반환 (현재 중복검증을 이메일 검증으로 할거라서 이메일로 찾음)
            return customOAuth2User;
        }

        // 2. 이메일로 사용자 조회 (기존세션으로 가입했는지?)

        Customer customerByEmail = (Customer) memberRepository.findByMemberEmail(oAuth2Response.getEmail());

        if (customerByEmail != null) {
            // 기존 이메일 계정이 있을 경우, OAuth 식별자를 추가하고 업데이트
            // 연동 해줌
            customerByEmail.getOauthIdentifiers().add(oauthIdentifier);
            memberRepository.save(customerByEmail);

            return new CustomOAuth2User(oAuth2Response);
        }

//        if (customerByEmail != null) {
//            boolean userConsent = // 프론트엔드에서 동의 여부를 받아오는 로직
//              // js에서 받아온 사용자의 동의
//            if (userConsent) {
//                customerByEmail.getOauthIdentifiers().add(oauthIdentifier);
//                memberRepository.save(customerByEmail);
//                return new CustomOAuth2User(oAuth2Response, customerByEmail.getMemberRole());
//            } else {
//                // 동의하지 않으면 다른 처리를 하거나 에러를 반환
//                throw new OAuth2AuthenticationException("Account linking was not consented by the user.");
//            }
//        }

        // 3: 새로운 사용자 생성

        MemberRole memberRole = extractRoleFromAuthorities(customOAuth2User.getAuthorities());

        Customer newCustomer = Customer.builder()
                .memberName(customOAuth2User.getName()) // name (네이버 OAuth)에서 받음
                .customerNickName(customOAuth2User.getNickname()) // nickname (네이버 OAuth)에서 받음
                .memberPassword(bCryptPasswordEncoder.encode("oauth"))// oauth전용 비밀번호 설정 (로그인할땐 막음)
                .memberPhone(customOAuth2User.getMobile()) // memberPhone (네이버 OAuth)에서 받음
                .memberEmail(customOAuth2User.getEmail()) // email (네이버 OAuth)에서 받음
                .memberStatus(MemberStatus.APPROVED) // 항상 승인
                .oauthIdentifiers(new HashSet<>()) // 명시적으로 초기화 (필요한 경우)
                .joinDate(LocalDate.now()) // 가입신청한 현재 시간
                .imageGenLeft(3) // 기본 3
                .memberRole(memberRole) // CUSTOMER (OAuth 로그인은 CUSTOMER만 됨)
                .customerGender(Gender.NONE) // 성별
                .customerProfileImage(basicProfileImageUrl)// 기본이미지
                .build();

        newCustomer.getOauthIdentifiers().add(oauthIdentifier); // OAuth 가입하면 넣는 식별값 넣기
        memberRepository.save(newCustomer);



        return new CustomOAuth2User(oAuth2Response);
    }
}


