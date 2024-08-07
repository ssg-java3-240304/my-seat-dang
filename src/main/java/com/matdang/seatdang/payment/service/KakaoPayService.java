package com.matdang.seatdang.payment.service;

import com.matdang.seatdang.payment.dto.*;
import com.matdang.seatdang.payment.entity.PayApprove;
import com.matdang.seatdang.payment.entity.PayReady;
import com.matdang.seatdang.payment.repository.KakaoPayRepository;
import com.matdang.seatdang.payment.repository.PayApproveRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
@Transactional
@RequiredArgsConstructor
public class KakaoPayService {
    private final KakaoPayRepository kakaoPayRepository;
    private final PayApproveRepository payApproveRepository;

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
                .approvalUrl("http://localhost:8080/" + "payment/approve" + "?PartnerOrderId="
                        + payDetail.getPartnerOrderId() + "&PartnerUserId="
                        + payDetail.getPartnerUserId()) // 결제 성공 시 redirect url, 최대 255자
                .cancelUrl("http://localhost:8080/" + "payment/cancel") // 결제 취소 시 redirect url, 최대 255자
                .failUrl("http://localhost:8080/" + "payment/fail") //결제 실패 시 redirect url, 최대 255자

                .build();

        // 3. HttpEntity 생성
        HttpEntity<ReadyRequest> entityMap = new HttpEntity<>(readyRequest, headers);

        // 4. RestTemplate 사용
        ResponseEntity<ReadyResponse> response = new RestTemplate().postForEntity(
                KAKAO_PAY_READY_URL,
                entityMap,
                ReadyResponse.class);

        ReadyResponse readyResponse = response.getBody();

        // TODO : 로직 오류 가능성 존재
        // 저장
        kakaoPayRepository.save(PayReady.builder().tid(readyResponse.getTid())
                .partnerOrderId(readyRequest.getPartnerOrderId())
                .partnerUserId(readyRequest.getPartnerUserId()).build());

        // 5. 응답 처리
        return readyResponse;
    }

    public Object approve(ReadyRedirect readyRedirect) {
        // ready할 때 저장해놓은 TID로 승인 요청
        // Call “Execute approved payment” API by pg_token, TID mapping to the current payment transaction and other parameters.
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "SECRET_KEY " + secretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String tid = kakaoPayRepository.findTidByPartnerOrderIdAndPartnerUserId(readyRedirect.getPartnerOrderId(),
                readyRedirect.getPartnerUserId());
        log.debug("tid = {}", tid);
        log.debug("PartnerOrderId = {}", readyRedirect.getPartnerOrderId());
        log.debug("PartnerUserId = {}", readyRedirect.getPartnerUserId());
        log.debug("pg_token = {}", readyRedirect.getPg_token());


        // Request param
        ApproveRequest approveRequest = ApproveRequest.builder()
                .cid(cid)
                .tid(tid)
                .partnerOrderId(readyRedirect.getPartnerOrderId())
                .partnerUserId(readyRedirect.getPartnerUserId())
                .pgToken(readyRedirect.getPg_token())
                .build();

        // Send Request
        HttpEntity<ApproveRequest> entityMap = new HttpEntity<>(approveRequest, headers);
        try {
            ResponseEntity<PayApprove> response = new RestTemplate().postForEntity(
                    KAKAO_PAY_APPROVE_URL,
                    entityMap,
                    PayApprove.class
            );

            // 승인 결과를 저장한다.
            // save the result of approval
            PayApprove approveResponse = response.getBody();
            payApproveRepository.save(approveResponse);

            return approveResponse;
        } catch (HttpStatusCodeException ex) {
            return ex.getResponseBodyAs(ApproveFail.class);
        }
    }

}
