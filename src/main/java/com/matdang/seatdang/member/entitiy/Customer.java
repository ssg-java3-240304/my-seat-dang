package com.matdang.seatdang.member.entitiy;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
//@Setter(AccessLevel.PRIVATE)
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("CUSTOMER")
public class Customer extends Member {
    private int imageGenLeft ; // ai생성 남은 횟수

    @Enumerated(EnumType.STRING)
    private Gender customerGender;

    private LocalDate customerBirthday;
    private String customerNickName; // 닉네임
    private String customerProfileImage; // 일반회원프로필이미지URL
}
