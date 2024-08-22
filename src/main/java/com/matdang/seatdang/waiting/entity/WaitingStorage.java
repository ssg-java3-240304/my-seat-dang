package com.matdang.seatdang.waiting.entity;

import com.matdang.seatdang.payment.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
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
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class WaitingStorage extends StorageBaseEntity implements Persistable<Long> {

    @Id
    private Long id;

    private Long waitingNumber;
    private Long waitingOrder;
    private Long storeId;

    @Embedded
    private CustomerInfo customerInfo;

    private LocalDateTime createdDate;

    @Enumerated(EnumType.STRING)
    private WaitingStatus waitingStatus;
    private LocalDateTime visitedTime;
    private LocalDateTime canceledTime;

    public WaitingStorage(Long id, Long waitingNumber, Long waitingOrder, Long storeId, Long customerId,
                          String customerPhone, Integer peopleCount, LocalDateTime createdDate,
                          WaitingStatus waitingStatus, LocalDateTime visitedTime, LocalDateTime canceledTime) {
        this.id = id;
        this.waitingNumber = waitingNumber;
        this.waitingOrder = waitingOrder;
        this.storeId = storeId;
        this.customerInfo = new CustomerInfo(customerId, customerPhone, peopleCount);
        this.createdDate = createdDate;
        this.waitingStatus = waitingStatus;
        this.visitedTime = visitedTime;
        this.canceledTime = canceledTime;
    }

    @Override
    public boolean isNew() {
        return getSavedDate() == null;
    }
}
