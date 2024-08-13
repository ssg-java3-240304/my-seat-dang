package com.matdang.seatdang.chat.service;

import com.matdang.seatdang.chat.entity.Chat;
import com.matdang.seatdang.chat.repository.ChatRepository;
import com.matdang.seatdang.member.repository.MemberRepository;
import com.matdang.seatdang.object_storage.service.FileService;
import com.matdang.seatdang.reservation.repository.ReservationRepository;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@Getter
@Setter
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository; // MongoDB Repository
    private final FileService fileService;

    @Value("${hostUrl}")
    private String serverUrl;

    public Mono<Chat> saveChatWithImage(Chat chat, MultipartFile customerImage) {
        // 이미지 업로드
        if (customerImage != null && !customerImage.isEmpty()) {
            String filePath = "chat-member-image";
            String imageUrl = fileService.uploadSingleFile(customerImage, filePath);
            chat.setCustomerImage(imageUrl);
        }

        chat.setCreatedAt(LocalDateTime.now());
        return chatRepository.save(chat);
    }
}
