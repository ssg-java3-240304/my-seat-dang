package com.matdang.seatdang.payment.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Setter(AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PayReady extends BaseEntity implements Persistable<String> {
    @Id
    private String partnerOrderId;
    private String tid;
    private String partnerUserId;
    private Long shopId;

    @Override
    public String getId() {
        return partnerOrderId;
    }

    @Override
    public boolean isNew() {
        return getCreatedDate() == null;
    }
}
