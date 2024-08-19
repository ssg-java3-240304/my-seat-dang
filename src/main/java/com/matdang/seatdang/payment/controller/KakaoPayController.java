package com.matdang.seatdang.payment.controller;

import com.matdang.seatdang.payment.controller.dto.ApproveSuccessResponse;
import com.matdang.seatdang.payment.controller.dto.RefundSuccessResponse;
import com.matdang.seatdang.payment.dto.*;
import com.matdang.seatdang.payment.entity.PayApprove;
import com.matdang.seatdang.payment.entity.RefundResult;
import com.matdang.seatdang.payment.service.KakaoPayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/payment")
public class KakaoPayController {

    private final KakaoPayService kakaoPayService;

    @GetMapping("/main")
    public void pay() {
    }

    private static int count = 0;
    private static Long partnerUserID = 1L;
    private static Long partnerOrderID = 1L;
    private static Long shopId = 1L;

    // refund test용
    private static String tid;


    @GetMapping("/request")
    public String readyToKakaoPay(@ModelAttribute PayDetail payDetail, Model model) {
        // test code
        payDetail = PayDetail.builder()
                .itemName("초코파이")
                .partnerUserId(Long.toString(partnerUserID++))
                .partnerOrderId(Long.toString(partnerOrderID++))
                .taxFreeAmount(0)
                .quantity(2)
                .shopId(shopId++)
                .totalAmount(2000 + count++)
                .build();

        ReadyResponse readyResponse = kakaoPayService.ready(payDetail);
        // refund test용
        tid = readyResponse.getTid();
        log.debug("ReadyResponse ={}", readyResponse);
        log.info("==== payment request ====");

        model.addAttribute("response", readyResponse);

        return "payment/ready";
    }

    // TODO : 새로고침 방지
    @GetMapping("/approve")
    public String approve(ReadyRedirect readyRedirect, Model model) {
        log.debug("=== approve start ===");

        Object approveResponse = kakaoPayService.approve(readyRedirect);
        log.debug("approveResponse type={}", approveResponse.getClass());

        if (approveResponse instanceof PayApprove) {
            log.info("=== approve success ===");

            model.addAttribute("response", ApproveSuccessResponse.create((PayApprove) approveResponse));
            return "payment/approve";
        }
        if (approveResponse instanceof ApproveFail) {
            log.error("=== Payment Approve Fail ===");
            log.error("{}", approveResponse);

            ((ApproveFail) approveResponse).registerStatus(Status.PAYMENT_FAILED);
            model.addAttribute("response", approveResponse);
            return "payment/approve-fail";
        }

        return null;
    }

    @GetMapping("/refund")
    public String refund(@ModelAttribute RefundDetail refundDetail, Model model) {
        refundDetail = refundDetail.builder()
                .partnerUserId(partnerUserID)
                .partnerOrderId(partnerOrderID)
                .tid(tid)
                .cancelAmount(1000)
                .cancelTaxFreeAmount(0)
                .build();

        Object refundResult = kakaoPayService.refund(refundDetail);
        log.debug("refundResult type={}", refundResult.getClass());

        if (refundResult instanceof RefundResult) {
            log.info("=== Refund success ===");

            model.addAttribute("response", RefundSuccessResponse.create((RefundResult) refundResult));
            return "payment/refund";
        }
        if (refundResult instanceof ApproveFail) {
            log.error("=== Refund Approve Fail ===");
            log.error("{}", refundResult);

            ((ApproveFail) refundResult).registerStatus(Status.REFUND_FAILED);
            model.addAttribute("response", refundResult);
            return "redirect:/payment/approve-fail";
        }

        return null;
    }

    @GetMapping("/cancel")
    public String cancel() {
        // 주문건이 진짜 취소되었는지 확인 후 취소 처리
        // 결제내역조회(/v1/payment/status) api에서 status를 확인한다.
        // To prevent the unwanted request cancellation caused by attack,
        // the “show payment status” API is called and then check if the status is QUIT_PAYMENT before suspending the payment
        return "payment/cancel";
    }
    @GetMapping("/fail")
    public String fail() {
        // 주문건이 진짜 실패되었는지 확인 후 실패 처리
        // 결제내역조회(/v1/payment/status) api에서 status를 확인한다.
        // To prevent the unwanted request cancellation caused by attack,
        // the “show payment status” API is called and then check if the status is FAIL_PAYMENT before suspending the payment
        return "payment/fail";
    }
}
