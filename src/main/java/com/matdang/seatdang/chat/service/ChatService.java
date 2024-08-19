package com.matdang.seatdang.chat.service;

import com.matdang.seatdang.chat.dto.ChatPaymentRequestDto;
import com.matdang.seatdang.chat.entity.Chat;
import com.matdang.seatdang.chat.repository.ChatRepository;
import com.matdang.seatdang.object_storage.service.FileService;
import lombok.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository; // MongoDB Repository
    private final FileService fileService;

    public Mono<Chat> saveChatWithImage(Chat chat, MultipartFile customerImage) {
        // 결제 정보가 있는 경우, 채팅 메시지에 결제 정보를 추가
//        if (chatPaymentRequestDto != null) {
//            chat.setItemName(chatPaymentRequestDto.getItemName());
//            chat.setQuantity(chatPaymentRequestDto.getQuantity());
//            chat.setTotalAmount(chatPaymentRequestDto.getTotalAmount());
//            System.out.println(chat);
//        }

        // 이미지 업로드 및 URL 설정
        if (customerImage != null && !customerImage.isEmpty()) {
            String filePath = "chat-member-image"; // 파일 저장 경로
            String imageUrl = fileService.uploadSingleFile(customerImage, filePath);
            chat.setCustomerImage(imageUrl); // 이미지 URL을 채팅 객체에 설정
        }
        chat.setCreatedAt(LocalDateTime.now());
        // 채팅 메시지 저장
        return chatRepository.save(chat)
                .doOnSuccess(savedChat -> System.out.println("Saved chat: " + savedChat))
                .doOnError(error -> System.err.println("Error saving chat: " + error));
    }
}
