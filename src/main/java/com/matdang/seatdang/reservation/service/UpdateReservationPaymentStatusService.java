package com.matdang.seatdang.reservation.service;

import com.matdang.seatdang.chat.dto.ChatPaymentInfoSaveRequestDto;
import com.matdang.seatdang.chat.service.ChatService;
import com.matdang.seatdang.payment.entity.PayApprove;
import com.matdang.seatdang.reservation.dto.ReservationResponseDto;
import com.matdang.seatdang.reservation.entity.Reservation;
import com.matdang.seatdang.reservation.repository.ReservationRepository;
import com.matdang.seatdang.reservation.vo.ReservationStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class UpdateReservationPaymentStatusService {
    private final ReservationCommandService reservationCommandService;
    private final ReservationQueryService reservationQueryService;
    private final ChatService chatService;

    public void updateToPayed(PayApprove payApprove){
        Long reservationId = Long.parseLong(payApprove.getPartnerOrderId());
        ReservationResponseDto reservationDto = reservationQueryService.getReservationById(reservationId);

        if(reservationDto == null){
            log.error("결제 후 예약 상태 변경에 실패했습니다. 사유: 예약 객체 DTO 를 얻지 못했습니다.");
            throw new RuntimeException("결제 후 예약 상태 변경에 실패했습니다");
        }

        if(reservationDto.getReservationStatus().equals(ReservationStatus.AWAITING_CUSTOM_PAYMENT)){
            //채팅 결제일 경우 결제 성공 메시지 전송

            chatService.savePaymentSuccess(ChatPaymentInfoSaveRequestDto.builder()
                    .sender(reservationDto.getCustomerName())
                    .roomNum(reservationDto.getReservationId().toString())
                    .totalAmount(payApprove.getAmount().getTotal())
                    .itemName(payApprove.getItemName())
                    .build());
        }
        reservationCommandService.updateStatusToBeforeVisit(reservationId);
    }
}
