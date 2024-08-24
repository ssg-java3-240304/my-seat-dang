package com.matdang.seatdang.reservation.service;

import com.matdang.seatdang.common.exception.ReservationException;
import com.matdang.seatdang.payment.controller.dto.ApproveSuccessResponse;
import com.matdang.seatdang.payment.dto.ApproveFail;
import com.matdang.seatdang.payment.dto.Status;
import com.matdang.seatdang.payment.entity.PayApprove;
import com.matdang.seatdang.reservation.dto.ReservationCancelRequestDto;
import com.matdang.seatdang.reservation.dto.ReservationSaveRequestDto;
import com.matdang.seatdang.reservation.dto.ReservationSlotReturnDto;
import com.matdang.seatdang.reservation.entity.Reservation;
import com.matdang.seatdang.reservation.repository.ReservationRepository;
import com.matdang.seatdang.reservation.vo.ReservationStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ReservationCommandService {
    private final ReservationRepository reservationRepository;
    private final ReservationSlotCommandService reservationSlotCommandService;

    public void createCustomMenuReservation(ReservationSaveRequestDto saveRequestDto) {
        log.debug("create empty reservation dto: {}", saveRequestDto);
        saveRequestDto.setReservationStatus(ReservationStatus.PENDING);
        boolean reservationExists =reservationRepository.findByCustomer_CustomerIdAndReservedAt(saveRequestDto.getCustomer().getCustomerId(), LocalDateTime.of(saveRequestDto.getDate(),saveRequestDto.getTime())).isPresent();
        if (reservationExists) {
            throw new ReservationException("같은 시간에 중복된 예약이 있습니다");
        }
        reservationRepository.save(saveRequestDto.toEntity());
    }

    public void updateStatusCustomReservation(Long reservationId, ReservationStatus reservationStatus) {
        log.debug("update reservation status service: {}", reservationId);
        Optional<Reservation> optReservation = reservationRepository.findById(reservationId);
        Reservation reservation = optReservation.orElseThrow( ()-> new ReservationException("예약을 찾을수 없습니다"));
        reservation.updateStatus(reservationStatus);
    }

    public void cancelReservation(ReservationCancelRequestDto cancelRequestDto) {
        log.debug("cancel reservation service: {}", cancelRequestDto);
        Optional<Reservation> optReservation = reservationRepository.findById(cancelRequestDto.getReservationId());
        Reservation reservation = optReservation.orElseThrow( ()-> new ReservationException("예약을 찾을수 없습니다"));
        reservation.cancel(cancelRequestDto.convertToReservationCancellationRecord());

        //예약 슬롯 반환
        reservationSlotCommandService.returnSlot(ReservationSlotReturnDto.builder()
                .storeId(reservation.getStore().getStoreId())
                .date(reservation.getReservedAt().toLocalDate())
                .time(reservation.getReservedAt().toLocalTime())
                .build());
    }


    public void updateStatusToBeforeVisit(Long reservationId){
        updateStatusCustomReservation(reservationId, ReservationStatus.BEFORE_VISIT);
    }

    public void updateStatusToAwaitingCustomPayment(Long reservationId) {
        updateStatusCustomReservation(reservationId, ReservationStatus.AWAITING_CUSTOM_PAYMENT);
    }
}
