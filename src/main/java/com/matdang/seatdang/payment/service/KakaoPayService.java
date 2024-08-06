package com.matdang.seatdang.payment.service;

import com.matdang.seatdang.payment.dto.ApproveRequest;
import com.matdang.seatdang.payment.dto.PayDetail;
import com.matdang.seatdang.payment.dto.ReadyRequest;
import com.matdang.seatdang.payment.dto.ReadyResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class KakaoPayService {
    @Value("${kakao.secret.key}")
    private String secretKey;
    @Value("${cid}")
    private String cid;
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
                .approvalUrl("http://localhost:8080/" + "payment/approve" ) // 결제 성공 시 redirect url, 최대 255자
                .cancelUrl("http://localhost:8080/" + "payment/cancel" ) // 결제 취소 시 redirect url, 최대 255자
                .failUrl("http://localhost:8080/" + "payment/fail" ) //결제 실패 시 redirect url, 최대 255자

                .build();

        // 3. HttpEntity 생성
        HttpEntity<ReadyRequest> entityMap = new HttpEntity<>(readyRequest, headers);

        // 4. RestTemplate 사용
        ResponseEntity<ReadyResponse> response = new RestTemplate().postForEntity(
                KAKAO_PAY_READY_URL,
                entityMap,
                ReadyResponse.class);

        ReadyResponse readyResponse = response.getBody();

        // 5. 응답 처리
        return readyResponse;
    }

    public String approve(String tid,  String pgToken) {
        // ready할 때 저장해놓은 TID로 승인 요청
        // Call “Execute approved payment” API by pg_token, TID mapping to the current payment transaction and other parameters.
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "SECRET_KEY " + secretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        log.debug("tid = {}", tid);

        // Request param
        ApproveRequest approveRequest = ApproveRequest.builder()
                .cid(cid)
                .tid(tid)
                .partnerOrderId("1")
                .partnerUserId("1")
                .pgToken(pgToken)
                .build();


        // Send Request
        HttpEntity<ApproveRequest> entityMap = new HttpEntity<>(approveRequest, headers);
        try {
            ResponseEntity<String> response = new RestTemplate().postForEntity(
                    KAKAO_PAY_APPROVE_URL,
                    entityMap,
                    String.class
            );

            // 승인 결과를 저장한다.
            // save the result of approval
            String approveResponse = response.getBody();
            return approveResponse;
        } catch (HttpStatusCodeException ex) {
            return ex.getResponseBodyAsString();
        }
    }

}
