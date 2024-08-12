package com.matdang.seatdang.auth.principal;

import com.matdang.seatdang.member.entitiy.StoreOwner;


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

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        Collection<GrantedAuthority> authorities = new ArrayList<>();
//        authorities.add(() -> "ROLE_STORE_OWNER"); // 특정 역할 설정
//        return authorities;
//    }
}

