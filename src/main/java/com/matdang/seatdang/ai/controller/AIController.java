package com.matdang.seatdang.ai.controller;


import com.matdang.seatdang.ai.dto.ErrorResponseDto;
import com.matdang.seatdang.ai.dto.GeneratedImageResponseDto;
import com.matdang.seatdang.ai.entity.GeneratedImageUrl;
import com.matdang.seatdang.ai.repository.GeneratedImageUrlRepository;
import com.matdang.seatdang.ai.service.AIService;
import com.matdang.seatdang.auth.service.AuthService;
import com.matdang.seatdang.member.entity.Customer;
import com.matdang.seatdang.member.service.CustomerService;
import com.matdang.seatdang.object_storage.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class AIController {

    private final AIService aiService;
    private final CustomerService customerService;
    private final GeneratedImageUrlRepository generatedImageUrlRepository;
    private final AuthService authService;


//    @GetMapping("/ai-image")
//    public String getImage(Model model)
//            throws IOException, InterruptedException {
//        String prompt = aiService.
////                generatePictureV2("케이크 시안을 원하는데 동그란시트 겨울왕국 올라프케이크 생성해줘");
//                generatePictureV2("케이크 시안을 원하는데 아빠가 좋아할거같은 케이크 시안 부탁해");
//        model.addAttribute("url", prompt);
//        return "ai/test";
//    }

    // test 용도
//    @GetMapping("/ai-image")
//    public String getImage(Model model) throws IOException, InterruptedException {
//        String imageUrl = aiService.generatePictureV2("케이크 시안을 원하는데 아빠가 좋아할거같은 케이크 시안 부탁해");
//
//        // 파일 저장 경로 설정 (예: "ai-generated-images/케이크-시안.jpg")
//        String filePath = "ai-generated-images/cake-idea.jpg";
//
//        // 이미지 URL을 통해 S3에 업로드
//        String uploadedImageUrl = aiService.uploadImageToS3(imageUrl, filePath);
//
//        // 업로드된 이미지 URL을 모델에 추가하여 뷰에 전달
//        model.addAttribute("url", uploadedImageUrl);
//
//        return "ai/test";
//    }




    @PostMapping("/ai/generate")
    @ResponseBody
    public ResponseEntity<?> generateImage(@RequestParam("cakeDescription") String cakeDescription) throws IOException, InterruptedException {
        Long customerId = authService.getAuthenticatedMember().getMemberId();
        Customer customer = customerService.findById(customerId);

        if (customer.getImageGenLeft() <= 0) {
            ErrorResponseDto errorResponse = new ErrorResponseDto("생성 가능한 이미지 횟수가 부족합니다.", LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }


        // AI 이미지 생성 및 생성된 이미지 NCP에 저장
        GeneratedImageUrl generatedImage = aiService.createAndSaveGeneratedImage(customerId, cakeDescription);

        // 이미지 생성 가능 횟수 차감
        customerService.decrementImageGenLeft(customer);

        // 응답 데이터 생성
        GeneratedImageResponseDto responseDto = new GeneratedImageResponseDto(
                generatedImage.getGeneratedUrl(),
                generatedImage.getInputText(),
                generatedImage.getCreatedAt()
        );

        return ResponseEntity.ok(responseDto);
    }







    @GetMapping("/ai-result")
    public String showResult(Model model){

        // 현재 로그인된 고객 정보
        Long customerId = authService.getAuthenticatedMember().getMemberId();

        // 고객이 생성한 모든 이미지 조회
        List<GeneratedImageUrl> imageList = generatedImageUrlRepository.findAllByCustomerId(customerId);

        // 모델에 이미지 목록 추가
        model.addAttribute("imageList", imageList);

        return "ai/ai-result";
    }
}