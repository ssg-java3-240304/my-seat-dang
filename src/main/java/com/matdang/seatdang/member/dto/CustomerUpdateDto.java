package com.matdang.seatdang.member.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CustomerUpdateDto {
    private String customerNickName;
    private String customerGender;
    private String customerBirthday;
    private MultipartFile customerProfileImage; // 업로드된 프로필 이미지 파일
}
