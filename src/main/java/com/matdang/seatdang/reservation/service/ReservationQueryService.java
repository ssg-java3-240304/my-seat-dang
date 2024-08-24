package com.matdang.seatdang.reservation.service;

import com.matdang.seatdang.auth.principal.MemberUserDetails;
import com.matdang.seatdang.reservation.dto.ReservationResponseDto;
import com.matdang.seatdang.reservation.dto.ResponseDto;
import com.matdang.seatdang.reservation.entity.Reservation;
import com.matdang.seatdang.reservation.repository.ReservationRepository;
import com.matdang.seatdang.reservation.vo.ReservationTicket;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationQueryService {
    private final ReservationRepository reservationRepository;

    public ReservationResponseDto getReservationById(Long id) {
        Reservation reservation = reservationRepository.findById(id).orElse(null);
        if (reservation == null) {
            return null;
        }
        return convertToResponseDto(reservation);
    }

    public List<ReservationResponseDto> getReservationsByCustomerId(Long customerId) {
        MemberUserDetails userDetails = (MemberUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Reservation> reservations = reservationRepository.findByCustomer_CustomerId(userDetails.getId());
        return reservations.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    private ReservationResponseDto convertToResponseDto(Reservation reservation) {
        ReservationResponseDto responseDto = new ReservationResponseDto();
        responseDto.setReservationId(reservation.getId());
        responseDto.setCustomerName(reservation.getCustomer().getCustomerName());
        responseDto.setCustomerId(reservation.getCustomer().getCustomerId());
        responseDto.setStoreId(reservation.getStore().getStoreId());
        responseDto.setStoreName(reservation.getStore().getStoreName());
        responseDto.setStoreOwnerId(reservation.getStoreOwner().getStoreOwnerId());
        responseDto.setStoreOwnerName(reservation.getStoreOwner().getStoreOwnerName());
        responseDto.setStorePhoneNumber(reservation.getStore().getStorePhone());
        responseDto.setCreatedAt(reservation.getCreatedAt());
        responseDto.setReservedAt(reservation.getReservedAt());
        responseDto.setOrderedMenuList(reservation.getOrderedMenuList());
        responseDto.setReservationStatus(reservation.getReservationStatus());
        responseDto.setThumbnail(reservation.getStore().getThumbnail());
        return responseDto;
    }
}
