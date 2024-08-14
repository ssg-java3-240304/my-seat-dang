package com.matdang.seatdang.auth.service;

import com.matdang.seatdang.auth.dto.CustomOAuth2User;
import com.matdang.seatdang.auth.principal.CustomerUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


/**
 *
 * 인증된 사용자의 정보를 가져와 비즈니스 로직에 활용함.
 *
 */

@Service
public class AuthService {

    //사용자가 로그인 한 후 인증된 상태에서 호출 되는 메서드들이다.

    public CustomerUserDetails getAuthenticatedUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof CustomerUserDetails) {
            return (CustomerUserDetails) authentication.getPrincipal();
        } else {
            return null;
        }
    }
    // OAuth2 로그인 사용자 정보를 가져오는 메서드
    public CustomOAuth2User getAuthenticatedOAuth2UserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof CustomOAuth2User) {
            return (CustomOAuth2User) authentication.getPrincipal();
        } else {
            return null;
        }
    }






}
