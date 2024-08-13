package com.matdang.seatdang.auth.dto;

import java.util.Map;

/**
 * 네이버 데이터 : JSON
 *
 * {
 * 		resultcode=00, message=success, response={id=123123123, name=김미미}
 * }
 *
 */


//    name,email,nickname,mobile 받아옴

public class NaverResponse implements OAuth2Response {


    private final Map<String,Object> attribute;

        public NaverResponse(Map<String, Object> attributes) {

            this.attribute = (Map<String, Object>) attributes.get("response");

    }

    // 기본적으로 모두 String으로 받아옴.

    @Override
    public String getProvider() {
        return "naver"; // naver라서
    }

    @Override
    public String getProviderId() {
        return attribute.get("id").toString();
    }

    @Override
    public String getEmail() {
        return attribute.get("email").toString();
    }

    @Override
    public String getName() {
        return attribute.get("name").toString();
    }

    //    name,email,nickname,mobile 받아옴

    // 선택

    public String getNickname() {
        return attribute.get("nickname").toString();
    }

    public String getMobile() {
        return attribute.get("mobile").toString();
    }

}
