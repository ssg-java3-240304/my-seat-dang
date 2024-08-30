package com.matdang.seatdang.reservation.repository;

import com.matdang.seatdang.reservation.entity.ReservationSlot;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

public interface ReservationSlotRepository extends JpaRepository<ReservationSlot, Long> {

    @Query("""
        SELECT r
        FROM ReservationSlot r
        WHERE r.reservationSlotId.storeId = :storeId
            AND r.reservationSlotId.date = :date
            AND r.reservationSlotId.time = :time
        """)
    Optional<ReservationSlot> findByStoreAndDateAndTime(@Param("storeId") Long storeId,
                                                                 @Param("date") LocalDate date,
                                                                 @Param("time") LocalTime time);
}
