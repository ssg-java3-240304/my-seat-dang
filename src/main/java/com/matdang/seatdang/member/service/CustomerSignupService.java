package com.matdang.seatdang.member.service;

import com.matdang.seatdang.member.dto.CustomerSignupDto;
import com.matdang.seatdang.member.entitiy.Customer;
import com.matdang.seatdang.member.entitiy.MemberRole;
import com.matdang.seatdang.member.entitiy.MemberStatus;
import com.matdang.seatdang.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CustomerSignupService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public CustomerSignupService(MemberRepository memberRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.memberRepository = memberRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    /**
     * 회원가입할 때 memberEmail,memberName,memberPassword,memberPhone,customerGender,customerBirthday,customerProfileImage 받음
     * joinDate,memberRole,memberStatus,imageGenLeft는 service에서 넣어주기
     *
     */

    public void joinProcess(CustomerSignupDto customerSignupDto){

        System.out.println(customerSignupDto);
        System.out.println(customerSignupDto.getMemberPassword());

        Customer customerEntity = Customer.builder()
                .memberName(customerSignupDto.getMemberName())
                .customerNickName(customerSignupDto.getCustomerNickName())
                .memberPassword(bCryptPasswordEncoder.encode(customerSignupDto.getMemberPassword()))
                .memberPhone(customerSignupDto.getMemberPhone())
                .memberEmail(customerSignupDto.getMemberEmail())
                .customerProfileImage(customerSignupDto.getCustomerProfileImage()) // 프로필이미지
                .customerGender(customerSignupDto.getCustomerGender())
                .customerBirthday(customerSignupDto.getCustomerBirthday())
                .imageGenLeft(3) // 기본 3
                .joinDate(LocalDate.now()) // 가입신청한 현재시간
                .memberStatus(MemberStatus.APPROVED) // Customer는 항상 승인
                .memberRole(MemberRole.ROLE_CUSTOMER) // 역할 : Customer
                .build();

        memberRepository.save(customerEntity);



    }

}
