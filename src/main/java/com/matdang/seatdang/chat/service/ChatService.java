package com.matdang.seatdang.chat.service;

import com.matdang.seatdang.chat.entity.Chat;
import com.matdang.seatdang.chat.repository.ChatRepository;

import com.matdang.seatdang.object_storage.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository; // MongoDB Repository
    private final FileService fileService;

    public Flux<Chat> findByRoomNum(String roomNum) {
        return chatRepository.FindByRoomNum(roomNum)
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Chat> saveChatWithImage(Chat chat, MultipartFile customerImage) {

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

    public Flux<Chat> getMessagesWithPagination(String roomNum, int limit, String beforeId) {
        if (beforeId == null || beforeId.isEmpty()) {
            // 처음 20개의 메시지를 불러오는 경우 (최신 메시지부터)
            return chatRepository.findTopByRoomNumOrderByCreatedAtDesc(roomNum, limit);
        } else {
            // 이전 메시지(스크롤 시 더 불러오는 경우)
            return chatRepository.findBeforeIdByRoomNum(roomNum, beforeId, limit);
        }
    }
}
