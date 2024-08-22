package com.matdang.seatdang.reservation.controller;

import com.matdang.seatdang.ai.entity.GeneratedImageUrl;
import com.matdang.seatdang.ai.repository.GeneratedImageUrlRepository;
import com.matdang.seatdang.auth.principal.MemberUserDetails;
import com.matdang.seatdang.auth.service.AuthService;
import com.matdang.seatdang.chat.chatconfig.ChatConfig;
import com.matdang.seatdang.member.dto.StoreOwnerResponseDto;
import com.matdang.seatdang.member.entity.Member;
import com.matdang.seatdang.member.service.CustomerService;
import com.matdang.seatdang.member.service.StoreOwnerMemberService;
import com.matdang.seatdang.reservation.dto.ReservationSaveRequestDto;
import com.matdang.seatdang.reservation.dto.ReservationTicketRequestDTO;
import com.matdang.seatdang.reservation.dto.ResponseDto;
import com.matdang.seatdang.reservation.service.ReservationCommandService;
import com.matdang.seatdang.reservation.service.ReservationService;
import com.matdang.seatdang.reservation.service.ReservationSlotCommandService;
import com.matdang.seatdang.reservation.vo.CustomerInfo;
import com.matdang.seatdang.reservation.vo.ReservationTicket;
import com.matdang.seatdang.reservation.vo.StoreOwnerInfo;
import com.matdang.seatdang.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
@RequestMapping("/my-seat-dang/reservation")
@RequiredArgsConstructor
public class ReservationCustomerController {
    private final ReservationCommandService reservationCommandService;
    private final ReservationSlotCommandService reservationSlotCommandService;
    private final StoreService storeService;
    private final CustomerService customerService;
    private final StoreOwnerMemberService storeOwnerMemberService;
    private final AuthService authService;
    private final ReservationService reservationService;
    private final GeneratedImageUrlRepository generatedImageUrlRepository;
    private final ChatConfig chatConfig;  // ChatConfig를 의존성 주입으로 받음
//    private final

    @GetMapping("/list")
    public String reservedPage(Model model) {

//         SecurityContext에서 고객 ID를 가져옴
        Long customerId = ((MemberUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        String chatUrl = chatConfig.getServerUrl();

        // 예약 목록 가져오기
        List<ResponseDto> reservations = reservationService.getReservationsByCustomerId(customerId);

        // 고객이 생성한 모든 이미지 담기
        List<GeneratedImageUrl> imageList = generatedImageUrlRepository.findAllByCustomerId(customerId);

//        model.addAttribute("chatAccessUrl", chatUrl);
        model.addAttribute("reservations", reservations);
        model.addAttribute("imageList", imageList); // 회원이 생성한 ai 생성 이미지 추가
        model.addAttribute("chatAccessUrl",chatUrl);
        log.info("reservations = {}", reservations);
        log.info("url = {}", chatUrl);
        return "customer/reservation/customer-reservationlist";
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
        //storeOwner, customer 정보를 추가로 설정해야합니다
        StoreOwnerResponseDto storeOwner = storeOwnerMemberService.findByStoreId(saveDto.getStore().getStoreId());
        saveDto.setStoreOwner(new StoreOwnerInfo(storeOwner.getStoreOwnerId(), storeOwner.getStoreOwnerName()));
        Member member = authService.getAuthenticatedMember();
        saveDto.setCustomer(new CustomerInfo(member.getMemberId(), member.getMemberName(), member.getMemberPhone()));

        //상점에서 예약한도 가져오기
        int maxReservationInTime = storeService.getMaxReservationInTime(saveDto.getStore().getStoreId());
        ReservationTicketRequestDTO ticketRequestDTO = ReservationTicketRequestDTO.builder()
                .storeId(saveDto.getStore().getStoreId())
                .date(saveDto.getDate())
                .time(saveDto.getTime())
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

    @PostMapping("/test")
    public String test(@ModelAttribute ReservationSaveRequestDto saveRequestDto){
        log.debug("saveRequest Test Dto: {}", saveRequestDto.toString());
        return "redirect:/my-seat-dang/store/detail/6";
    }
}
