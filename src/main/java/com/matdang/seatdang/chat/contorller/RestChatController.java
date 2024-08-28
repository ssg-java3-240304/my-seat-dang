package com.matdang.seatdang.chat.contorller;

import com.matdang.seatdang.chat.entity.Chat;
import com.matdang.seatdang.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;


@RequiredArgsConstructor
@RestController // 데이터 리턴 서버
public class RestChatController {
    private final ChatService chatService;

    // SSE 엔드포인트 추가
    @GetMapping(value = "/chat/roomNum/{roomNum}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Chat> streamMessages(@PathVariable String roomNum) {
        LocalDateTime connectionTime = LocalDateTime.now(); // SSE 연결 시간

        return chatService.findByRoomNum(roomNum)
                .filter(chat -> chat.getCreatedAt().isAfter(connectionTime)) // 연결 이후의 메시지만 필터링
                .subscribeOn(Schedulers.boundedElastic());
    }

    @CrossOrigin
    @GetMapping(value = "/chat/roomNum/{roomNum}/messages", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Chat> getMessagesWithPagination(
            @PathVariable String roomNum,
            @RequestParam("limit") int limit,
            @RequestParam(value = "beforeId", required = false) String beforeId) {

        return chatService.getMessagesWithPagination(roomNum, limit, beforeId)
                .subscribeOn(Schedulers.boundedElastic())
                .switchIfEmpty(Flux.empty()); // Flux가 비어 있을 경우 빈 Flux 반환
    }
    @CrossOrigin(origins = "*")
    @PostMapping("/chat")
    public Mono<Chat> setMsg(@RequestPart(value = "chat") Chat chat,
                             @RequestPart(value = "customerImage", required = false) MultipartFile customerImage
                             ) {
        System.out.println(chat);
        System.out.println(customerImage);
        return chatService.saveChatWithImage(chat, customerImage);
    }

}
