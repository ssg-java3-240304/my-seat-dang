package com.matdang.seatdang.chat.repository;

import com.matdang.seatdang.chat.entity.Chat;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
@Repository
public interface ChatRepository extends ReactiveMongoRepository<Chat, String>{
    @Tailable // 커서를 안닫고 계속 유지한다.
    @Query("{sender: ?0, receiver: ?1}")
    Flux<Chat> FindBySender(String sender, String receiver); // flux 흐름 response를 유지하면서 데이터를 계속 보냄

    @Tailable // 커서를 안닫고 계속 유지한다.
    @Query("{roomNum: ?0}")
    Flux<Chat> FindByRoomNum(String roomNum);
}
