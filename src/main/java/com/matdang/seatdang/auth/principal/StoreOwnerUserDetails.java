package com.matdang.seatdang.auth.principal;

import com.matdang.seatdang.member.entitiy.StoreOwner;
import com.matdang.seatdang.member.vo.StoreVo;


/**
 *
 * 현재 로그인된 세션의 정보(StoreOwner)입니다
 *
 */

public class StoreOwnerUserDetails extends MemberUserDetails {

    public StoreOwnerUserDetails(StoreOwner memberData) {
        super(memberData);
    }

    public String getBusinessLicenseImage() {
        return ((StoreOwner) member).getBusinessLicenseImage();
    }

    public String getBusinessLicense() {
        return ((StoreOwner) member).getBusinessLicense();
    }

    public String getBankAccountCopy() {
        return ((StoreOwner) member).getBankAccountCopy();
    }

    public String getBankAccount() {
        return ((StoreOwner) member).getBankAccount();
    }

    public String getStoreOwnerProfileImage() {
        return ((StoreOwner) member).getStoreOwnerProfileImage();
    }

    // StoreVo 객체를 반환하는 메서드 추가
    public StoreVo getStore() {
        return ((StoreOwner) member).getStore();
    }


}

