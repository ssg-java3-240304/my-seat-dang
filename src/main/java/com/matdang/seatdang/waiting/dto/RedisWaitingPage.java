package com.matdang.seatdang.waiting.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.matdang.seatdang.waiting.repository.query.dto.WaitingInfoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true, value = {"pageable"})
public class RedisWaitingPage extends PageImpl<WaitingInfoDto> {
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public RedisWaitingPage(@JsonProperty("content") List<WaitingInfoDto> content,
                            @JsonProperty("number") int page,
                            @JsonProperty("size") int size,
                            @JsonProperty("totalElements") long total) {
        super(content, PageRequest.of(page, size), total);
    }

    public RedisWaitingPage(Page<WaitingInfoDto> page) {
        super(page.getContent(), page.getPageable(), page.getTotalElements());
    }
}