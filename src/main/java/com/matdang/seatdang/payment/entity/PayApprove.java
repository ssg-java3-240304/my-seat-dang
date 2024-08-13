package com.matdang.seatdang.payment.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import java.time.LocalDateTime;
import org.springframework.data.domain.Persistable;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Setter(AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PayApprove extends BaseEntity implements Persistable<String> {
    /**
     * 저장 해야할 것
     * 승인 테이블
     * "aid":"A6b2d0593aa5606e25ca",
     * "tid":"T6b2d03429fd342cfc75",
     * "partner_order_id":"1",
     * "partner_user_id":"1",
     * "item_name":"초코파이",
     * "item_code":null,  // 추가됨
     * "quantity":2,
     * "amount":{"total":2000,
     * "tax_free":0,
     * "vat":182,
     * "point":0,
     * "discount":0,
     * "green_deposit":0},
     * "payment_method_type":"MONEY",
     * "created_at":"2024-08-07T10:39:01",
     * "approved_at":"2024-08-07T10:39:40"}
     */
    @Id
    private String aid; // 현재는 삭제해도 무방
    private String tid;
    private String partnerOrderId;
    private String partnerUserId;
    private String itemName;
    private String itemCode;
    private int quantity;
    @Embedded
    private Amount amount;
    private String paymentMethodType;
    private LocalDateTime createdAt;
    private LocalDateTime approvedAt;
    private Long shopId;

    public void registerShopId(Long shopId) {
        this.shopId = shopId;
    }

    @Override
    public String getId() {
        return aid;
    }

    @Override
    public boolean isNew() {
        return getCreatedDate() == null;
    }
}
