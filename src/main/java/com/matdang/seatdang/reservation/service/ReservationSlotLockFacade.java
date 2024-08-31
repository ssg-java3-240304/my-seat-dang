package com.matdang.seatdang.reservation.service;

import com.matdang.seatdang.reservation.dto.ReservationSlotReturnDto;
import com.matdang.seatdang.reservation.dto.ReservationTicketRequestDTO;
import com.matdang.seatdang.reservation.repository.ReservationSlotLockRepository;
import com.matdang.seatdang.reservation.vo.ReservationTicket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationSlotLockFacade {
    private final ReservationSlotLockRepository lockRepository;
    private final ReservationSlotCommandService reservationSlotCommandService;

    @Transactional
    public ReservationTicket getTicket(ReservationTicketRequestDTO ticketRequestDTO) {
        String key = "slot-" + ticketRequestDTO.getStoreId() + ticketRequestDTO.getDate() + ticketRequestDTO.getTime();
        ReservationTicket ticket = null;
        try {
            // 락선점
            Integer hasLock = lockRepository.getLock(key);
            // 락을 획득한 경우에만 예약슬롯에 접근 가능
            if(hasLock > 0) {
                ticket = reservationSlotCommandService.getReservationTicket(ticketRequestDTO);
            }else
                throw new RuntimeException("예약슬롯 락 획득에 실패했습니다");
        } finally {
            // 락해제
            lockRepository.releaseLock(key);
        }
        return ticket;
    }

    @Transactional
    public void releaseSlot(ReservationSlotReturnDto requestDTO) {
        String key = "slot-" + requestDTO.getStoreId() + requestDTO.getDate() + requestDTO.getTime();
        try {
            // 락선점
            Integer hasLock = lockRepository.getLock(key);
            // 락을 획득한 경우에만 예약슬롯에 접근 가능
            if(hasLock > 0) {
                reservationSlotCommandService.releaseSlot(requestDTO);
            }else
                throw new RuntimeException("예약슬롯 락 획득에 실패했습니다");
        } finally {
            // 락해제
            lockRepository.releaseLock(key);
        }
    }
}
