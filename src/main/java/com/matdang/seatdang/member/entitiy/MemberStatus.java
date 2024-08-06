package com.matdang.seatdang.member.entitiy;

/**
 * 회원 상태를 나타내는 열거형 클래스입니다.
 *
 *
 * DEACTIVATED: 탈퇴회원</li>
 * PENDING_APPROVAL: 가입승인진행중</li>
 * APPROVED: 가입승인</li>
 * WITHDRAWN: 가입철회</li>
 *
 */


public enum MemberStatus {

    // Customer 회원은 회원가입하면 무조건 가입승인상태 (
    // StoreOwner 회원은 회원가입하면 가입승인진행중 (PENDING_APPROVAL)
    DEACTIVATED, // 탈퇴회원
    PENDING_APPROVAL, // 가입승인진행중
    APPROVED, // 가입승인
    WITHDRAWN // 가입철회
}
