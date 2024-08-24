package com.matdang.seatdang.chat.repository;

import com.matdang.seatdang.chat.entity.Chat;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ChatRepository extends ReactiveMongoRepository<Chat, String>{

    @Tailable // 커서를 안닫고 계속 유지한다.
    @Query("{roomNum: ?0}")
    Flux<Chat> FindByRoomNum(String roomNum);


    // 최신 메시지부터 정렬하여 상위 limit 개만 가져오는 쿼리
    Flux<Chat> findTopByRoomNumOrderByCreatedAtDesc(String roomNum, int limit);

    // 특정 ID 이전의 메시지들을 가져오는 쿼리
    Flux<Chat> findBeforeIdByRoomNum(String roomNum, String beforeId, int limit);
}
