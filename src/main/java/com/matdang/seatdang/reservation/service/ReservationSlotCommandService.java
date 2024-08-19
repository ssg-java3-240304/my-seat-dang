package com.matdang.seatdang.reservation.service;

import com.matdang.seatdang.reservation.dto.ReservationTicketRequestDTO;
import com.matdang.seatdang.reservation.entity.ReservationSlot;
import com.matdang.seatdang.reservation.repository.ReservationSlotRepository;
import com.matdang.seatdang.reservation.vo.ReservationTicket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Slf4j
@Service
@Transactional(isolation = Isolation.REPEATABLE_READ)
@RequiredArgsConstructor
public class ReservationSlotCommandService {
    private final ReservationSlotRepository reservationSlotRepository;

    public synchronized ReservationTicket getReservationTicket(ReservationTicketRequestDTO requestDTO) {
        Optional<ReservationSlot> optSlot = reservationSlotRepository.findByStoreAndDateAndTime(requestDTO.getStoreId(), requestDTO.getDate(), requestDTO.getTime());

        ReservationSlot slot = optSlot.orElseGet(()->{
            // 조회된 데이터가 없는 경우 새로운 ReservationSlot을 생성
            ReservationSlot newSlot = new ReservationSlot(
                    requestDTO.getStoreId()
                    ,requestDTO.getDate()
                    ,requestDTO.getTime()
                    ,requestDTO.getMaxReservation());
            // 생성된 객체를 저장
                return reservationSlotRepository.save(newSlot);
            });

        if(slot.getUsedSlots()<slot.getMaxReservation()) {
            return slot.tryAddSlot();
        }else{
            return ReservationTicket.UNAVAILABLE;
        }
    }
}
