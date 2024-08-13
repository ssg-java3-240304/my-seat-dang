package com.matdang.seatdang.payment.controller.dto;

import com.matdang.seatdang.payment.entity.PayApprove;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApproveSuccessResponse {
    private String partnerOrderId;
    private String itemName;
    private int quantity;
    private int totalAmount;
    private String paymentMethodType;
    private LocalDateTime approvedAt;


    public static ApproveSuccessResponse create(PayApprove payApprove) {
        ApproveSuccessResponse approveSuccessResponse = new ApproveSuccessResponse();
        approveSuccessResponse.partnerOrderId = payApprove.getPartnerOrderId();
        approveSuccessResponse.itemName = payApprove.getItemName();
        approveSuccessResponse.quantity = payApprove.getQuantity();
        approveSuccessResponse.totalAmount = payApprove.getAmount().getTotal();
        approveSuccessResponse.paymentMethodType = payApprove.getPaymentMethodType();
        approveSuccessResponse.approvedAt = payApprove.getApprovedAt();

        return approveSuccessResponse;
    }
}
