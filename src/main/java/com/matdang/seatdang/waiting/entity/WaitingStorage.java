package com.matdang.seatdang.waiting.entity;

import com.matdang.seatdang.payment.entity.BaseEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "waiting_storage_sequence")
    @GenericGenerator(
            name = "waiting_storage_sequence",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "waiting_storage_sequence"),
                    @Parameter(name = "optimizer", value = "pooled"),
                    @Parameter(name = "initial_value", value = "1"),
                    @Parameter(name = "increment_size", value = "1000")
            }
    )
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
