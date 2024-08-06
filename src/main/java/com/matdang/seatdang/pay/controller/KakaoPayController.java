package com.matdang.seatdang.pay.controller;


import com.matdang.seatdang.pay.dto.PayDetail;
import com.matdang.seatdang.pay.dto.ReadyResponse;
import com.matdang.seatdang.pay.service.KakaoPayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RequiredArgsConstructor
@Controller
@RequestMapping("/pay")
public class KakaoPayController {

    private final KakaoPayService kakaoPayService;

    @GetMapping("/main")
    public void pay() {

    }

    @GetMapping("/readyToKakaoPay/pay")
    public String readyToKakaoPay(Model model) {
        PayDetail payDetail = (PayDetail) model.getAttribute("PayDetail");
        ReadyResponse readyResponse = kakaoPayService.ready(payDetail);

        // pc
        model.addAttribute("response", readyResponse);
        return "/ready";
    }

//    @GetMapping("/approve/{openType}")
//    public String approve( @PathVariable("openType") String openType, @RequestParam("pg_token") String pgToken, Model model) {
//        String approveResponse = kakaoPayService.approve(pgToken);
//        model.addAttribute("response", approveResponse);
//        return  openType + "/approve";
//    }
//
//    @GetMapping("/readyToKakaoPay/refund/{openType}")
//    public String refund( @PathVariable("openType") String openType, Model model) {
//        String refundRequest = kakaoPayService.refund();
//        model.addAttribute("response", refundRequest);
//        return  openType + "/refund";
//    }
//
//    @GetMapping("/cancel/{openType}")
//    public String cancel( @PathVariable("openType") String openType) {
//        // 주문건이 진짜 취소되었는지 확인 후 취소 처리
//        // 결제내역조회(/v1/payment/status) api에서 status를 확인한다.
//        // To prevent the unwanted request cancellation caused by attack,
//        // the “show payment status” API is called and then check if the status is QUIT_PAYMENT before suspending the payment
//        return  openType + "/cancel";
//    }
//
//    @GetMapping("/fail/{agent}/{openType}")
//    public String fail(@PathVariable("openType") String openType) {
//        // 주문건이 진짜 실패되었는지 확인 후 실패 처리
//        // 결제내역조회(/v1/payment/status) api에서 status를 확인한다.
//        // To prevent the unwanted request cancellation caused by attack,
//        // the “show payment status” API is called and then check if the status is FAIL_PAYMENT before suspending the payment
//        return openType + "/fail";
//    }
}
