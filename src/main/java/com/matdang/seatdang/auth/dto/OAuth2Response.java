package com.matdang.seatdang.auth.dto;

public interface OAuth2Response {

    String getProvider(); // 제공자 (ex naver , goole)
    String getProviderId(); // 제공자에서 발급해주는 아이디(번호)
    String getEmail(); // 이메일
    String getName(); // 사용자 실명
}
