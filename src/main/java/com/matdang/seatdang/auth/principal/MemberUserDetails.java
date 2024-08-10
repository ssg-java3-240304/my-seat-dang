package com.matdang.seatdang.auth.principal;

import com.matdang.seatdang.member.entitiy.Member;
import com.matdang.seatdang.member.entitiy.MemberStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

public abstract class MemberUserDetails implements UserDetails {

    protected final Member member;

    public MemberUserDetails(Member memberData) {
        this.member = memberData;
    }

    @Override
    public String getUsername() {
        return member.getMemberEmail();
    }

    @Override
    public String getPassword() {
        return member.getMemberPassword();
    }

    public String getRealName() {
        return member.getMemberName();
    }

    public String getMemberPhone() {
        return member.getMemberPhone();
    }

    public LocalDate getJoinDate() {
        return member.getJoinDate();
    }

    public MemberStatus getMemberStatus() {
        return member.getMemberStatus();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    //ROLE
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(() -> member.getMemberRole().name());
        return authorities;
    }
}

