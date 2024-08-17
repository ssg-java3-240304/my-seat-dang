package com.matdang.seatdang.auth.service;

import com.matdang.seatdang.auth.dto.CustomOAuth2User;
import com.matdang.seatdang.auth.principal.CustomerUserDetails;
import com.matdang.seatdang.member.entity.Member;
import com.matdang.seatdang.auth.principal.StoreOwnerUserDetails;
import com.matdang.seatdang.member.repository.MemberRepository;
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

    private final MemberRepository memberRepository;

    public AuthService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
    public Member getAuthenticatedMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof CustomerUserDetails) {
                return ((CustomerUserDetails) principal).getMember(); // 세션 로그인 사용자
            } else if (principal instanceof CustomOAuth2User) {
                String email = ((CustomOAuth2User) principal).getEmail();
                return memberRepository.findByMemberEmail(email); // OAuth2 로그인 사용자
            }
        }

        return null; // 인증된 사용자가 없으면 null 반환
    }

    public Long getAuthenticatedStoreId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof StoreOwnerUserDetails) {
                return  ((StoreOwnerUserDetails) principal).getStore().getStoreId();
            }
        }

        return null;
    }
}
