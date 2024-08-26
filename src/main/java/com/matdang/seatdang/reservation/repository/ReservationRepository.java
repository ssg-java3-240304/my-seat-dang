package com.matdang.seatdang.reservation.repository;

import com.matdang.seatdang.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<Reservation> findByCustomer_CustomerIdAndReservedAt(Long customerId, LocalDateTime reservedAt);
    List<Reservation> findByCustomer_CustomerId(Long customerId);
    List<Reservation> findByStoreOwner_StoreOwnerId(Long storeOwnerId);
}
