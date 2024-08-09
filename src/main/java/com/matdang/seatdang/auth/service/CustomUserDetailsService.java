package com.matdang.seatdang.auth.service;

import com.matdang.seatdang.auth.dto.CustomUserDetails;
import com.matdang.seatdang.member.entitiy.Member;
import com.matdang.seatdang.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {


    private final MemberRepository memberRepository;

    @Autowired
    public CustomUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member memberData = memberRepository.findByMemberEmail(username);

        if (memberData != null) {
            return new CustomUserDetails(memberData);
        }
        return null;
    }
}
