package com.matdang.seatdang.ai.service;



import com.amazonaws.services.s3.model.ObjectMetadata;
import com.matdang.seatdang.ai.dto.GeneratedImageUrlDto;
import com.matdang.seatdang.ai.entity.GeneratedImageUrl;
import com.matdang.seatdang.ai.repository.GeneratedImageUrlRepository;
import com.matdang.seatdang.object_storage.service.FileService;
import com.theokanning.openai.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CakeDesignService {

    private final OpenAiService openAiService;
    private final GeneratedImageUrlRepository generatedImageUrlRepository;
    private final FileService fileService;

    @Value("${openai.api.key}")
    private String openaiApiKey;


    /**
     *
     * 이미지생성 메서드
     * @param prompt
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public String generatePictureV2(String prompt) throws IOException, InterruptedException {
        String url = "https://api.openai.com/v1/images/generations";

        // 한국어로 된 케이크 관련 프롬프트로 변환 (어떤 입력값이던 케이크만 나오게)
        String cakeSpecificPrompt = String.format(
                "이 이미지는 오직 케이크 디자인을 나타냅니다. %s. 케이크",
                prompt
        );

        // JSON 문자열 생성
        String requestBody = String.format(
                "{\"model\":\"dall-e-3\",\"prompt\":\"%s\",\"n\":1,\"size\":\"1024x1024\"}",
                cakeSpecificPrompt);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + openaiApiKey)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // 응답 본문에서 URL 추출
        String responseBody = response.body();
        int startIndex = responseBody.indexOf("https://"); // URL이 "https://"로 시작함
        int endIndex = responseBody.indexOf("\"", startIndex); // URL이 큰 따옴표로 끝남
        String imageUrl = responseBody.substring(startIndex, endIndex);

        log.info(response.body());
        log.info("===============");
        log.info(imageUrl);

        return imageUrl;
    }

    public String uploadImageToS3(String imageUrl, String filePath) throws IOException {
        // 이미지 URL에서 InputStream을 얻어오기
        try (InputStream inputStream = new URL(imageUrl).openStream()) {
            // 파일 메타데이터 설정
            ObjectMetadata metadata = new ObjectMetadata();

            // 이미지 파일 확장자에 따른 Content-Type 설정
            if (imageUrl.endsWith(".png")) {
                metadata.setContentType("image/png");
            } else if (imageUrl.endsWith(".jpg") || imageUrl.endsWith(".jpeg")) {
                metadata.setContentType("image/jpeg");
            }

            // InputStream에서 실제 바이트 길이를 계산
            byte[] imageBytes = inputStream.readAllBytes();
            metadata.setContentLength(imageBytes.length);

            // InputStream을 다시 생성 (S3 업로드를 위해)
            try (InputStream uploadStream = new ByteArrayInputStream(imageBytes)) {
                // FileService를 이용해 S3에 업로드
                return fileService.uploadFile(uploadStream, filePath, metadata);
            }
        }
    }


    public GeneratedImageUrl createAndSaveGeneratedImage(Long customerId, String cakeDescription) throws IOException, InterruptedException {
        // AI 이미지 생성
        String imageUrl = generatePictureV2(cakeDescription);

        // S3 또는 NCP에 업로드
        String filePath = "ai-generated-images/" + customerId + "/cake-idea.jpg";
        String uploadedImageUrl = uploadImageToS3(imageUrl, filePath);

        // 이미지 URL 및 생성된 데이터 저장
        GeneratedImageUrl generatedImage = GeneratedImageUrl.builder()
                .customerId(customerId)
                .generatedUrl(uploadedImageUrl)
                .createdAt(LocalDateTime.now())
                .inputText(cakeDescription)
                .build();

        generatedImageUrlRepository.save(generatedImage);

        return generatedImage;
    }

    public List<GeneratedImageUrlDto> getGeneratedImagesByCustomerId(Long customerId) {
        List<GeneratedImageUrl> imageList = generatedImageUrlRepository.findAllByCustomerId(customerId);

        // 엔티티를 DTO로 변환
        return imageList.stream()
                .map(image -> new GeneratedImageUrlDto(
                        image.getGeneratedUrl(),
                        image.getCreatedAt(),
                        image.getInputText()))
                .toList();
    }

//    // 속도 체크 메소드
//    public void testExecutionTimeWithAverage() throws IOException, InterruptedException {
//        int iterations = 10;
//        long totalTime = 0;
//
//        for (int i = 0; i < iterations; i++) {
//            long startTime = System.nanoTime();
//
//            // 실행할 코드 (예: API 호출)
//            createAndSaveGeneratedImage(1L,"말티즈 케이크");
//
//            long endTime = System.nanoTime();
//            long duration = (endTime - startTime) / 1_000_000; // 밀리초로 변환
//            totalTime += duration;
//            System.out.println("Iteration " + (i + 1) + " 실행 시간: " + duration + "ms");
//        }
//
//        long averageTime = totalTime / iterations;
//        System.out.println("평균 실행 시간: " + averageTime + "ms");
//    }









}
