package com.matdang.seatdang.waiting.controller.dto;


import com.matdang.seatdang.waiting.repository.query.dto.WaitingInfoDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class PageRangeDto {
    private int startPage;
    private int endPage;

    public static <T> PageRangeDto calculatePage(Page<T> page) {
        int totalPages = page.getTotalPages();
        int currentPage = page.getNumber();

        // 페이지 버튼 범위 계산
        int pageRange = 5;
        int startPage = Math.max(0, currentPage - pageRange / 2);
        int endPage = Math.min(totalPages - 1, currentPage + pageRange / 2);

        if (endPage - startPage + 1 < pageRange) {
            if (currentPage < totalPages / 2) {
                endPage = Math.min(totalPages - 1, endPage + (pageRange - (endPage - startPage + 1)));
            } else {
                startPage = Math.max(0, startPage - (pageRange - (endPage - startPage + 1)));
            }
        }

        return new PageRangeDto(startPage, endPage);
    }
}
