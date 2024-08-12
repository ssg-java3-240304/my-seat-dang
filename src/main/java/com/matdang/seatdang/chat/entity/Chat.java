package com.matdang.seatdang.chat.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "chat") // 컬렉션 chat으로 지정
public class Chat {
    @Id
    private String id; // 자동으로 생성되는 bson 몽고 id
    private String msg; // 컨텐츠 내용
    private String sender; // 보내는 사람 : 회원 -- 보류
    private String receiver; // 받는 사람 : 점주 -- 보류
    private String roomNum; // 방 번호

    private String customerImage; // 사용자가 점주에게 보낼 시안 사진
    private LocalDateTime createdAt; // 채팅을 친 시각
}
