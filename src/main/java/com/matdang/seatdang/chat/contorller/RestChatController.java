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


@RequiredArgsConstructor
@RestController // 데이터 리턴 서버
public class RestChatController {
    private final ChatService chatService;

    @CrossOrigin
    @GetMapping(value = "/chat/roomNum/{roomNum}/messages", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Chat> getMessagesWithPagination(
            @PathVariable String roomNum,
            @RequestParam("limit") int limit,
            @RequestParam(value = "beforeId", required = false) String beforeId) {

        return chatService.getMessagesWithPagination(roomNum, limit, beforeId)
                .subscribeOn(Schedulers.boundedElastic());
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
