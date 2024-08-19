package com.matdang.seatdang.reservation.service;

import com.matdang.seatdang.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class ReservationCommandService {
    private final ReservationRepository reservationRepository;

    public void createEmptyReservation() {

    }
}
