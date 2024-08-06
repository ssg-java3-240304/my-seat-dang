package com.matdang.seatdang.payment.service;

import com.matdang.seatdang.payment.dto.PayDetail;
import com.matdang.seatdang.payment.dto.ReadyRequest;
import com.matdang.seatdang.payment.dto.ReadyResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoPayService {
    @Value("${kakao.secret.key}")
    private String secretKey;
    @Value("${cid}")
    private String cid;
    private String tid;
    private static final String KAKAO_PAY_READY_URL = "https://open-api.kakaopay.com/online/v1/payment/ready";
    private static final String KAKAO_PAY_APPROVE_URL = "https://open-api.kakaopay.com/online/v1/payment/approve";
    private static final String KAKAO_PAY_CANCEL_URL = "https://open-api.kakaopay.com/online/v1/payment/cancel";

    public ReadyResponse ready(PayDetail payDetail) {

        // 1. 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "DEV_SECRET_KEY " + secretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 2. 요청 본문 설정
        ReadyRequest readyRequest = ReadyRequest.builder()
                .cid(cid)
                .partnerOrderId(payDetail.getPartnerOrderId())
                .partnerUserId(payDetail.getPartnerUserId())
                .itemName(payDetail.getItemName())
                .quantity(payDetail.getQuantity())
                .totalAmount(payDetail.getTotalAmount())
                .taxFreeAmount(payDetail.getTaxFreeAmount())
                .approvalUrl("http://localhost:8080/" + "approve/" ) // 결제 성공 시 redirect url, 최대 255자
                .cancelUrl("http://localhost:8080/" + "cancel/" ) // 결제 취소 시 redirect url, 최대 255자
                .failUrl("http://localhost:8080/" + "fail/" ) //결제 실패 시 redirect url, 최대 255자

                .build();

        // 3. HttpEntity 생성
        HttpEntity<ReadyRequest> entityMap = new HttpEntity<>(readyRequest, headers);

        // 4. RestTemplate 사용
        ResponseEntity<ReadyResponse> response = new RestTemplate().postForEntity(
                KAKAO_PAY_READY_URL,
                entityMap,
                ReadyResponse.class);

        ReadyResponse readyResponse = response.getBody();

        this.tid = readyResponse.getTid();
        // 5. 응답 처리
        return readyResponse;
    }
}
