package com.matdang.seatdang.member.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.LocalDate;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
//@Setter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DTYPE")
@Table(name = "tbl_member")
@SuperBuilder(toBuilder = true) //상속된 필드들을 빌더 패턴으로 초기화
//@Builder
public abstract class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_id")
    private Long memberId;
    @Column(nullable = false)
    private String memberEmail;
    @Column(nullable = false)
    private LocalDate joinDate;
    @Column(nullable = false)
    private String memberName;
    @Column(nullable = false)
    private String memberPassword;
    @Column(nullable = false)
    private String memberPhone;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberStatus memberStatus;

}
