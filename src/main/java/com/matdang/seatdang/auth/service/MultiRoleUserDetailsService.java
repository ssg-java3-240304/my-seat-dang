package com.matdang.seatdang.auth.service;

import com.matdang.seatdang.auth.principal.AdminUserDetails;
import com.matdang.seatdang.auth.principal.CustomerUserDetails;
import com.matdang.seatdang.auth.principal.StoreOwnerUserDetails;
import com.matdang.seatdang.member.entitiy.Admin;
import com.matdang.seatdang.member.entitiy.Customer;
import com.matdang.seatdang.member.entitiy.Member;
import com.matdang.seatdang.member.entitiy.StoreOwner;
import com.matdang.seatdang.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * 사용자가 로그인 할 때 사용자의 인증 정보를 로드함.(인증 절차의 일부)
 *
 *
 */



@Service
public class MultiRoleUserDetailsService implements UserDetailsService {


    private final MemberRepository memberRepository;

    @Autowired
    public MultiRoleUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member memberData = memberRepository.findByMemberEmail(username);

        if (memberData instanceof Customer) {
            return new CustomerUserDetails((Customer) memberData); // Customer
        } else if (memberData instanceof StoreOwner) {
            return new StoreOwnerUserDetails((StoreOwner) memberData); // StoreOwner
        } else if (memberData instanceof Admin) {
            return new AdminUserDetails((Admin) memberData); // admin
        } else {
            throw new UsernameNotFoundException("지원되지 않는사용자: " + username);
        }
    }
}
