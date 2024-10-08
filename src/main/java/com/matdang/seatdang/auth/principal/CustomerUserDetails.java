package com.matdang.seatdang.auth.principal;

import com.matdang.seatdang.member.entity.Customer;
import com.matdang.seatdang.member.entity.Gender;

import java.time.LocalDate;

/**
 *
 * 현재 로그인된 세션의 정보(Customer)입니다
 *
 */



public class CustomerUserDetails extends MemberUserDetails {

    public CustomerUserDetails(Customer memberData) {
        super(memberData);
    }

    public String getCustomerNickName() {
        return ((Customer) member).getCustomerNickName();
    }

    public String getCustomerProfileImage() {
        return ((Customer) member).getCustomerProfileImage();
    }

    public Gender getCustomerGender() {
        return ((Customer) member).getCustomerGender();
    }

    public LocalDate getCustomerBirthday() {
        return ((Customer) member).getCustomerBirthday();
    }

    // Member 객체를 반환
    public Customer getMember() {
        return (Customer) this.member;
    }


//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        Collection<GrantedAuthority> authorities = new ArrayList<>();
//        authorities.add(() -> "ROLE_CUSTOMER"); // 특정 역할 설정
//        return authorities;
//    }


}
