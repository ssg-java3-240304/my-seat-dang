package com.matdang.seatdang.payment.controller.dto;

import com.matdang.seatdang.payment.entity.RefundResult;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RefundSuccessResponse {
    private Long partnerOrderId;
    private String itemName;
    private Integer quantity;
    private Integer payAmount;
    private String paymentMethodType;
    private LocalDateTime approvedAt;
    private Integer approvedCancelAmount;
    private Integer canceledTotalAmount;
    private String status;
    private LocalDateTime canceledAt;

    public static RefundSuccessResponse create(RefundResult refundResult) {
        RefundSuccessResponse refundSuccessResponse = new RefundSuccessResponse();
        refundSuccessResponse.partnerOrderId = refundResult.getPartnerOrderId();
        refundSuccessResponse.itemName = refundResult.getItemName();
        refundSuccessResponse.status = refundResult.getStatus();
        refundSuccessResponse.quantity = refundResult.getQuantity();
        refundSuccessResponse.payAmount = refundResult.getAmount().getTotal();
        refundSuccessResponse.paymentMethodType = refundResult.getPaymentMethodType();
        refundSuccessResponse.approvedAt = refundResult.getApprovedAt();
        refundSuccessResponse.approvedCancelAmount = refundResult.getApprovedCancelAmount().getTotal();
        refundSuccessResponse.canceledTotalAmount = refundResult.getCanceledAmount().getTotal();
        refundSuccessResponse.canceledAt = refundResult.getCanceledAt();

        return refundSuccessResponse;
    }
}
