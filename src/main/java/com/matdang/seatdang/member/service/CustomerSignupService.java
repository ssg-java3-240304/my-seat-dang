package com.matdang.seatdang.member.service;

import com.matdang.seatdang.member.dto.CustomerSignupDto;
import com.matdang.seatdang.member.entitiy.Customer;
import com.matdang.seatdang.member.entitiy.MemberRole;
import com.matdang.seatdang.member.entitiy.MemberStatus;
import com.matdang.seatdang.member.repository.MemberRepository;
import com.matdang.seatdang.object_storage.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Service
public class CustomerSignupService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final FileService fileService; // File 업로드를 위해

    @Autowired
    public CustomerSignupService(MemberRepository memberRepository, BCryptPasswordEncoder bCryptPasswordEncoder,FileService fileService) {
        this.memberRepository = memberRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.fileService = fileService;

    }


    /**
     * 회원가입할 때 memberEmail,memberName,memberPassword,memberPhone,customerGender,customerBirthday,customerProfileImage 받음
     * joinDate,memberRole,memberStatus,imageGenLeft는 service에서 넣어주기
     *
     */

    public void signupProcess(CustomerSignupDto customerSignupDto,MultipartFile customerProfileImage){

        // 파일 업로드 처리
        String uploadedUrl = fileService.uploadSingleFile(customerProfileImage, "customer-profile-images"); // 타입 String으로 바꾸기
        System.out.println(uploadedUrl); // 파일 업로드 확인
        System.out.println(customerSignupDto);

        Customer customerEntity = Customer.builder()
                .memberName(customerSignupDto.getMemberName())
                .customerNickName(customerSignupDto.getCustomerNickName())
                .memberPassword(bCryptPasswordEncoder.encode(customerSignupDto.getMemberPassword()))
                .memberPhone(customerSignupDto.getMemberPhone())
                .memberEmail(customerSignupDto.getMemberEmail())
                .customerProfileImage(uploadedUrl) // 업로드된 URL 사용
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
