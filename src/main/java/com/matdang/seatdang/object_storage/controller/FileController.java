package com.matdang.seatdang.object_storage.controller;

import com.matdang.seatdang.object_storage.model.dto.FileDto;
import com.matdang.seatdang.object_storage.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/storage")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    // 파일 업로드

    @GetMapping("/upload")
    public String getUpload(){
        return "/storage/upload";
    }

    @PostMapping("/upload")
    public ResponseEntity<Object> uploadFilesSample(
            @RequestPart(value = "files") List<MultipartFile> multipartFiles) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(fileService.uploadFilesSample(multipartFiles));
    }

    // 업로드 된 파일 불러오기 ( 대신 지금 업로드한 폴더만)
    @GetMapping("/files")
    public String listFiles(Model model) {
        List<FileDto> files = fileService.listFiles("sample-folder");
        model.addAttribute("files", files);
        return "/storage/list";
    }


}
