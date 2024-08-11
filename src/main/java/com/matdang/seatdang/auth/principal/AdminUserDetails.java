package com.matdang.seatdang.auth.principal;

import com.matdang.seatdang.member.entitiy.Admin;


/**
 *
 * 현재 로그인된 세션의 정보(Admin)입니다
 *
 */

public class AdminUserDetails extends MemberUserDetails {

    public AdminUserDetails(Admin memberData) {
        super(memberData);
    }


//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        Collection<GrantedAuthority> authorities = new ArrayList<>();
//        authorities.add(() -> "ROLE_ADMIN"); // 특정 역할 설정
//        return authorities;
//    }
}

