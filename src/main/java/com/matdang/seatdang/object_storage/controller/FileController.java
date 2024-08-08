package com.matdang.seatdang.object_storage.controller;

import com.matdang.seatdang.object_storage.model.dto.FileDto;
import com.matdang.seatdang.object_storage.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/storage")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    // 파일 업로드

    @GetMapping("/upload")
    public void getUpload(){
    }

    //테스트용도
//    @PostMapping("/upload")
//    public ResponseEntity<Object> uploadFilesSample(
//            @RequestPart(value = "files") List<MultipartFile> multipartFiles) {
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(fileService.uploadFilesSample(multipartFiles,"원하는폴더명")); // 원하는 폴더명 입력하기
//    }

    @PostMapping("/upload")
    public ResponseEntity<Object> uploadFiles(
            @RequestPart(value = "files") List<MultipartFile> multipartFiles) {
//        String folderName = "원하는 폴더명 입력";
        String folderName = "member";
        List<String> uploadedFileUrls = fileService.uploadFiles(multipartFiles, folderName);

        // 업로드한 파일들의 URL 리스트를 반환
        return ResponseEntity.status(HttpStatus.OK).body(uploadedFileUrls);
    }





//    // 업로드 된 파일 불러오기 ( 대신 지금 업로드한 폴더에 있는 사진 모두)
//    @GetMapping("/files")
//    public String listFiles(Model model) {
//        List<FileDto> files = fileService.listFiles("업로드한폴더명"); // 업로드한 폴더명 입력하기
//        model.addAttribute("files", files);
//        return "storage/list";
//    }

    @GetMapping("/files")
    public String listFiles(@RequestParam("folder") String folderName, Model model) {
        folderName = "member"; // 업로드한 폴더명
        List<FileDto> files = fileService.listFiles(folderName); // 특정 폴더의 파일만 가져오기
        model.addAttribute("files", files);
        return "storage/list";
    }





}
