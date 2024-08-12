//package com.matdang.seatdang.review.service;
//
//import com.matdang.seatdang.member.entitiy.Member;
//import com.matdang.seatdang.member.repository.MemberRepository;
//import com.matdang.seatdang.review.entity.Review;
//import com.matdang.seatdang.review.repository.ReviewRepository;
//import com.matdang.seatdang.review.requestDto.ReviewCreateRequest;
//import com.matdang.seatdang.store.entity.Store;
//import com.matdang.seatdang.store.repository.StoreRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@Transactional(readOnly = true)
//@RequiredArgsConstructor
//public class ReviewService {
//    private final ReviewRepository reviewRepository;
//    private final MemberRepository memberRepository;
//    private final StoreRepository storeRepository;
//
//
//    @Transactional
//    public Long createReview(ReviewCreateRequest request) {
//        Member member = memberRepository.findById(request.getMemberId()).orElseThrow();
//        Store store = storeRepository.findById(request.getStoreId()).orElseThrow();
//        Review reivew = Review.createReivew(member,store,request.getContent(),request.getImage(),request.getRate());
//
//        // 저장 되는 JPA 코드
//        reviewRepository.save(reivew);
//        return reivew.getId();
//    }
//
//
//
//    // 리뷰 생성 메서드.
//
//}
