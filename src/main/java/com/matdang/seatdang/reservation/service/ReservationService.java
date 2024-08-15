package com.matdang.seatdang.reservation.service;

import com.matdang.seatdang.auth.principal.MemberUserDetails;
import com.matdang.seatdang.auth.principal.StoreOwnerUserDetails;
import com.matdang.seatdang.customer.main.model.ResponseDto;
import com.matdang.seatdang.reservation.entity.Reservation;
import com.matdang.seatdang.reservation.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;

    public List<ResponseDto> getReservationsByCustomerId(Long customerId) {
        MemberUserDetails userDetails = (MemberUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Reservation> reservations = reservationRepository.findByCustomer_CustomerId(userDetails.getId());
        return reservations.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public List<ResponseDto> getReservationsByStoreOwnerId(Long storeOwnerId) {
        StoreOwnerUserDetails userDetails = (StoreOwnerUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Reservation> reservations = reservationRepository.findByStoreOwner_StoreOwnerId(userDetails.getId());
        return reservations.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }


    private ResponseDto convertToResponseDto(Reservation reservation) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setReservationId(reservation.getId());
        responseDto.setCustomerName(reservation.getCustomer().getCustomerName());
        responseDto.setCustomerId(reservation.getCustomer().getCustomerId());
        responseDto.setStoreId(reservation.getStore().getStoreId());
        responseDto.setStoreName(reservation.getStore().getStoreName());
        responseDto.setStoreOwnerId(reservation.getStoreOwner().getStoreOwnerId());
        responseDto.setStoreOwnerName(reservation.getStoreOwner().getStoreOwnerName());
        return responseDto;
    }
}
