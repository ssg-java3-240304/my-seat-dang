package com.matdang.seatdang.reservation.repository;

import com.matdang.seatdang.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByCustomer_CustomerId(Long customerId);
}
