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

import java.util.concurrent.Semaphore;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationSlotLockFacade {
    private final ReservationSlotLockRepository lockRepository;
    private final ReservationSlotCommandService reservationSlotCommandService;
    private final Semaphore semaphore = new Semaphore(3);

    public ReservationTicket getTicket(ReservationTicketRequestDTO ticketRequestDTO) {
        String key = "slot-" + ticketRequestDTO.getStoreId() + ticketRequestDTO.getDate() + ticketRequestDTO.getTime();
        ReservationTicket ticket = null;
        try {
            // 세마포어 획득 (트랜잭션 전에)
            semaphore.acquire();

            // 트랜잭션 내에서 DB 작업 실행
            ticket = getTicketWithTransaction(ticketRequestDTO, key);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            // 락 해제 및 세마포어 반환
            lockRepository.releaseLock(key);
            semaphore.release();
        }
        return ticket;
    }

    @Transactional
    protected ReservationTicket getTicketWithTransaction(ReservationTicketRequestDTO ticketRequestDTO, String key) {
        // 락 선점
        Integer hasLock = lockRepository.getLock(key);
        if (hasLock > 0) {
            return reservationSlotCommandService.getReservationTicket(ticketRequestDTO);
        } else {
            throw new RuntimeException("예약슬롯 락 획득에 실패했습니다");
        }
    }

    @Transactional
    public void releaseSlot(ReservationSlotReturnDto requestDTO) {
        String key = "slot-" + requestDTO.getStoreId() + requestDTO.getDate() + requestDTO.getTime();
        try {
            // 락 선점
            Integer hasLock = lockRepository.getLock(key);
            if (hasLock > 0) {
                reservationSlotCommandService.releaseSlot(requestDTO);
            } else {
                throw new RuntimeException("예약슬롯 락 획득에 실패했습니다");
            }
        } finally {
            // 락 해제
            lockRepository.releaseLock(key);
        }
    }
}
