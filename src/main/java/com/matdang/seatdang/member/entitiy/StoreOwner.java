package com.matdang.seatdang.member.entitiy;

import com.matdang.seatdang.member.entitiy.Vo.StoreVo;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter
//@Setter(AccessLevel.PRIVATE)
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("STORE_OWNER")
public class StoreOwner extends Member {
    private String businessLicenseImage ; // 사업자등록증 이미지 url
    private String businessLicense ; // 사업자등록번호
    private String bankAccountCopy; // 통장사본 이미지 url
    private String bankAccount; // 계좌번호

    @Embedded
    private StoreVo store;





}
