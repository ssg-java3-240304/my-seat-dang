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

    public long getId(){
        return member.getMemberId();
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

    /**
     *
     * 계정이 만료되지 않았는가?
     * - true (만료 x)
     * - false (만료 o)
     * @return
     */


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 계정이 잠겨있지 않는가?
     * -true (잠겨있지 않음)
     * -false(잠겨있음)
     *
     * @return
     */


    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     *
     * 비밀번호가 만료되지 않았는가?
     * -ture(만료되지 않았음)
     * -false(만료되었음)
     * @return
     */

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     *
     * 계정이 활성화 되었는가?
     * - true (활성화 되었음)
     * - false (비활성화 되었음)
     *
     * @return
     */


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

