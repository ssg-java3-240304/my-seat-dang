package com.matdang.seatdang.reservation.controller;

import com.matdang.seatdang.reservation.dto.ReservationSaveRequestDto;
import com.matdang.seatdang.reservation.dto.ReservationTicketRequestDTO;
import com.matdang.seatdang.reservation.service.ReservationCommandService;
import com.matdang.seatdang.reservation.service.ReservationSlotCommandService;
import com.matdang.seatdang.reservation.vo.ReservationTicket;
import com.matdang.seatdang.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/my-seat-dang/reservation")
@RequiredArgsConstructor
public class ReservationCustomerController {
    private final ReservationCommandService reservationCommandService;
    private final ReservationSlotCommandService reservationSlotCommandService;
    private final StoreService storeService;

    @GetMapping("list")
    public String list(Model model){
        return "/customer/mypage/mypage";
    }

    @PostMapping("create-custom")
    public String custom(@ModelAttribute("custom_reservation") ReservationSaveRequestDto saveDto, Model model) {
        //예약 한도 조회 Dto 세팅
        //컨트롤러에서 준비해야할 것
        /*
        storeInfo = storeId, storeName, storePhone
        customerInfo = customerId customerName customerPhone
        storeOwner = storeOwnerId storeOwnerName;
        reservedAt
        orderedMenuList#orderedMenu()
                    private String menuName;
                    private int menuPrice;
                    private String imageUrl;
                    private CustomMenuOpt customMenuOpt;
                            private String sheet;
                            private String size;
                            private String cream;
                            private String lettering;
         */
        //상점에서 예약한도 가져오기
        int maxReservationInTime = storeService.getMaxReservationInTime(saveDto.getStore().getStoreId());
        ReservationTicketRequestDTO ticketRequestDTO = ReservationTicketRequestDTO.builder()
                .storeId(saveDto.getStore().getStoreId())
                .date(saveDto.getReservedAt().toLocalDate())
                .time(saveDto.getReservedAt().toLocalTime())
                .maxReservation(maxReservationInTime)
                .build();
        //예약 한도 조회
        ReservationTicket ticket = reservationSlotCommandService.getReservationTicket(ticketRequestDTO);
        //예약 가능 키를 획득했으면 예약
        if(ticket.equals(ReservationTicket.AVAILABLE)){
            //예약 티켓 획득 성공시 처리
            reservationCommandService.createCustomMenuReservation(saveDto);
        }else{
            //예약 티켓 획득 실패시 처리
            throw new RuntimeException("이미 마감된 예약입니다");
        }
        return "redirect:/my-seat-dang/reservation/list";
    }
}
