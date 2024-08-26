package com.matdang.seatdang.chat.service;

import com.matdang.seatdang.chat.dto.ChatPaymentInfoSaveRequestDto;
import com.matdang.seatdang.chat.entity.Chat;
import com.matdang.seatdang.chat.repository.ChatRepository;
import com.matdang.seatdang.member.service.CustomerService;
import com.matdang.seatdang.object_storage.service.FileService;
import com.matdang.seatdang.reservation.service.ReservationCommandService;
import com.matdang.seatdang.reservation.service.ReservationQueryService;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository; // MongoDB Repository
    private final FileService fileService;
    private final ReservationCommandService reservationCommandService;
    private final CustomerService customerService;

    public Mono<Chat> saveChatWithImage(Chat chat, MultipartFile customerImage) {

        // 이미지 업로드 및 URL 설정
        if (customerImage != null && !customerImage.isEmpty()) {
            String filePath = "chat-member-image"; // 파일 저장 경로
            String imageUrl = fileService.uploadSingleFile(customerImage, filePath);
            chat.setCustomerImage(imageUrl); // 이미지 URL을 채팅 객체에 설정
        }
        chat.setCreatedAt(LocalDateTime.now());

        if(chat.getItemName() != null && !chat.getItemName().isEmpty()){
            log.debug("chatService : update reservation status");
            updateReservationStatusToAwaitingPayment(Long.parseLong(chat.getRoomNum()));
        }
        // 채팅 메시지 저장
        return chatRepository.save(chat)
                .doOnSuccess(savedChat -> System.out.println("Saved chat: " + savedChat))
                .doOnError(error -> System.err.println("Error saving chat: " + error));
    }

    public void savePaymentSuccess(ChatPaymentInfoSaveRequestDto dto) {

        if(dto.getMessage() == null) {
            dto.setMessage("결제에 성공했습니다");
        }

        Chat chat = Chat.builder()
                .msg(dto.getMessage())
                .sender(dto.getSender())
                .roomNum(dto.getRoomNum())
                .createdAt(LocalDateTime.now())
                .build();

        chatRepository.save(chat).subscribe();
    }

    private void updateReservationStatusToAwaitingPayment(Long reservationId){
        reservationCommandService.updateStatusToAwaitingCustomPayment(reservationId);
    }
}
