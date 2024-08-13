package com.matdang.seatdang.auth.dto;

import com.matdang.seatdang.member.entitiy.MemberRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private final OAuth2Response oAuth2Response;

    private final MemberRole role;

    public CustomOAuth2User(OAuth2Response oAuth2Response, MemberRole role) {
        this.oAuth2Response = oAuth2Response;
        this.role = role;
    }

    // 로그인 진행하면 resource 서버로 부터 넘어오는 모든 데이터
    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    //ROLE

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//
//        Collection<GrantedAuthority> collection = new ArrayList<>();
//
//        collection.add(new GrantedAuthority() {
//
//            @Override
//            public String getAuthority() {
//
//                return role;
//            }
//        });
//
//        return collection;
//    }

    // 사용자 권한 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(() -> role.name());
        return authorities;
    }


    // 사용자 이름 (OAuth2 응답에서 가져옴)
    @Override
    public String getName() {
        return oAuth2Response.getName() ;
    }

    // 이메일 반환 (애플리케이션의 사용자 식별 용도로 사용)
    public String getEmail() {
        return oAuth2Response.getEmail();
    }

    // 아이디 강제로 만들기
    //provider + providerId
    // OAuth2 제공자의 고유 식별자를 식별하기 위한 용도

    public String getUsername() {

        return oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();
    }


}
