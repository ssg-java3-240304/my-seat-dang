package com.matdang.seatdang.auth.service;

import com.matdang.seatdang.auth.dto.CustomerSignupDto;
import com.matdang.seatdang.member.entity.Customer;
import com.matdang.seatdang.member.entity.MemberRole;
import com.matdang.seatdang.member.entity.MemberStatus;
import com.matdang.seatdang.member.repository.CustomerRepository;
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
    private final CustomerRepository customerRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final FileService fileService; // File 업로드를 위해

    @Autowired
    public CustomerSignupService(MemberRepository memberRepository, BCryptPasswordEncoder bCryptPasswordEncoder,FileService fileService,CustomerRepository customerRepository) {
        this.memberRepository = memberRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.fileService = fileService;
        this.customerRepository = customerRepository;

    }


    /**
     * 회원가입할 때 memberEmail,memberName,memberPassword,memberPhone,customerGender,customerBirthday,customerProfileImage 받음
     * joinDate,memberRole,memberStatus,imageGenLeft는 service에서 넣어주기
     *
     */

    public void signupProcess(CustomerSignupDto customerSignupDto,MultipartFile customerProfileImage){

        // 기본 프로필 이미지 URL 설정
        String basicProfileImageUrl = "https://kr.object.ncloudstorage.com/myseatdang-bucket/member/3539241b-bf4c-474b-abdd-3d32f9841d9c.jpg";

        // 파일 업로드 처리 (회원가입할 때 업로드 했다면 업로드한 url, 아니면 basicProfileImageUrl)
        String uploadedUrl;
        if (customerProfileImage != null && !customerProfileImage.isEmpty()) {
            uploadedUrl = fileService.uploadSingleFile(customerProfileImage, "customer-profile-images");
        } else {
            uploadedUrl = basicProfileImageUrl;
        }

        //회원(Customer) 생성

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

    public boolean isNicknameDuplicate(String customerNickName) {
        return customerRepository.existsByCustomerNickName(customerNickName);
    }

    public boolean isEmailDuplicate(String memberEmail) {
        return memberRepository.existsByMemberEmail(memberEmail);
    }
}
