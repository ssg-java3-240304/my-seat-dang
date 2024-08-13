package com.matdang.seatdang.payment.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.matdang.seatdang.payment.entity.vo.Amount;
import com.matdang.seatdang.payment.entity.vo.ApprovedCancelAmount;
import com.matdang.seatdang.payment.entity.vo.CanceledAmount;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Setter(AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RefundResult extends BaseEntity implements Persistable<String> {

    //취소 결과(result of approval)
    //{"tid":"T6a8d38329fd342cdd38",
    // "cid":"TC0ONETIME",
    // "status":"PART_CANCEL_PAYMENT",
    // "partner_order_id":"1",
    // "partner_user_id":"1",
    // "payment_method_type":"CARD",
    // "item_name":"초코파이",
    // "aid":"A6a8d6a129fd342cdd3c",
    // "quantity":1,
    // "amount":{"total":1100000,
    // "tax_free":0,"vat":100000,
    // "point":0,"discount":0,
    // "green_deposit":0},
    // "canceled_amount":{"total":1100,"tax_free":0,"vat":100,"point":0,"discount":0,"green_deposit":0},
    // "cancel_available_amount":{"total":1098900,"tax_free":0,"vat":99900,"point":0,"discount":0,"green_deposit":0},
    // "approved_cancel_amount":{"total":1100,"tax_free":0,"vat":100,"point":0,"discount":0,"green_deposit":0},
    // "created_at":"2024-07-30T20:50:27",
    // "approved_at":"2024-07-30T20:51:01",
    // "canceled_at":"2024-07-30T21:03:45"}

    @Id
    private String aid;
    private Long partnerOrderId;
    private Long partnerUserId;
    private Long shopId;
    private String tid;
    private String status;
    private String paymentMethodType;
    private String itemName;
    private Integer quantity;
    @Embedded
    private Amount amount;
    @Embedded
    private CanceledAmount canceledAmount;
    @Embedded
    private ApprovedCancelAmount approvedCancelAmount;
    private LocalDateTime createdAt;
    private LocalDateTime approvedAt;
    private LocalDateTime canceledAt;

    public void registerShopId(Long shopId) {
        this.shopId = shopId;
    }


    @Override
    public String getId() {
        return getAid();
    }

    @Override
    public boolean isNew() {
        return getCreatedDate() == null;
    }
}
