package com.matdang.seatdang.waiting.dto;

import com.matdang.seatdang.waiting.repository.query.dto.WaitingInfoDto;
import java.util.List;
import lombok.Data;


@Data
public class WaitingInfoPageDto {
    private List<WaitingInfoDto> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
}