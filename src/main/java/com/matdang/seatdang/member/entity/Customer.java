package com.matdang.seatdang.member.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
//@Setter
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("CUSTOMER")
@SuperBuilder(toBuilder = true)
//@Builder
public class Customer extends Member {
    private int imageGenLeft ; // ai생성 남은 횟수

    @Enumerated(EnumType.STRING)
    private Gender customerGender;
    private LocalDate customerBirthday;
    private String customerNickName; // 닉네임
    private String customerProfileImage; // 일반회원프로필이미지URL
//    @Column(unique = true)
//    private String oauthIdentifier; // OAuth 고유 식별자 추가

    // 지금은 네이버만 이지만 추후 카카오 넣을 수도 있어서 set으로 받는다.
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "customer_oauth_identifiers", joinColumns = @JoinColumn(name = "member_id"))
    @Column(name = "oauth_identifier")
    private Set<String> oauthIdentifiers = new HashSet<>(); // OAuth 식별자 저장

}
