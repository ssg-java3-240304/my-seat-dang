package com.matdang.seatdang.review.controller;


import com.matdang.seatdang.review.requestDto.ReviewCreateRequest;
import com.matdang.seatdang.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;
    // store -> storeID
    // member -> memberID
    // 리뷰 컨텐트
    // 리뷰 이미지
    // 리뷰 rate

    @PostMapping("/createReview")
    public String createReview(@ModelAttribute ReviewCreateRequest request){
        Long createReviewId = reviewService.createReview(request);
        System.out.println(createReviewId + "번째 리뷰 생성");

        return null;
    }

    @GetMapping("/domain")
    public String main(){
        return "customer/review/domain";
    }


}

//상점이 여러개 -> 상점 ID, 내 ID, 그리고 Review 생성시 Review ID
// 상점 ID, 내 아이디랑, 내가 쓴 제목, 내가 쓴 코맨트 , 뭐 날짜 등등..