package com.matdang.seatdang.member.dto;

import com.matdang.seatdang.member.entitiy.Gender;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CustomerSignupDto {
    private String memberEmail; // 필수
    private String customerNickName; // 필수
    private String memberName; // 필수
    private String memberPassword; // 필수
    private String memberPhone; // 필수
    private String customerProfileImage; // 프로필이미지URL // 선택
    private Gender customerGender; // 선택
    private LocalDate customerBirthday; // 선택

}
