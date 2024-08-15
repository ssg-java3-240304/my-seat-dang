package com.matdang.seatdang.chat.contorller;

import com.matdang.seatdang.chat.entity.Chat;
import com.matdang.seatdang.chat.repository.ChatRepository;
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
    private final ChatRepository chatRepository;
    private final ChatService chatService;

    @CrossOrigin
    @GetMapping(value = "/chat/roomNum/{roomNum}",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Chat> findByRoomNum(@PathVariable String roomNum){
        return chatRepository.FindByRoomNum(roomNum)
                .subscribeOn(Schedulers.boundedElastic());
    }
    @CrossOrigin(origins = "*")
    @PostMapping("/chat")
    public Mono<Chat> setMsg(@RequestPart(value = "chat") Chat chat,
                             @RequestPart(value = "customerImage", required = false) MultipartFile customerImage) {
        return chatService.saveChatWithImage(chat, customerImage);
    }

}
