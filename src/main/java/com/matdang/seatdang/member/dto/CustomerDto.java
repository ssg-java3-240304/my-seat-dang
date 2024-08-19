package com.matdang.seatdang.member.dto;

import lombok.Data;

@Data
public class CustomerDto {

    private String memberName;
    private String customerNickName;
    private String customerProfileImage;
    private String memberEmail;
    private String memberPhone;
    private String customerGender;
    private String customerBirthday;
    private int imageGenLeft;
}
