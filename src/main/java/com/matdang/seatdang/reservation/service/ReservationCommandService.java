package com.matdang.seatdang.reservation.service;

import com.matdang.seatdang.reservation.dto.ReservationSaveRequestDto;
import com.matdang.seatdang.reservation.entity.Reservation;
import com.matdang.seatdang.reservation.repository.ReservationRepository;
import com.matdang.seatdang.reservation.vo.ReservationStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ReservationCommandService {
    private final ReservationRepository reservationRepository;

    public void createCustomMenuReservation(ReservationSaveRequestDto saveRequestDto) {
        log.debug("create empty reservation dto: {}", saveRequestDto);
        saveRequestDto.setReservationStatus(ReservationStatus.DETAILING);
        boolean reservationExists =reservationRepository.findByCustomer_CustomerIdAndReservedAt(saveRequestDto.getCustomer().getCustomerId(), saveRequestDto.getReservedAt()).isPresent();
        if (reservationExists) {
            throw new RuntimeException("같은 시간에 중복된 예약이 있습니다");
        }
        reservationRepository.save(saveRequestDto.toEntity());
    }
}
