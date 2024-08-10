package com.matdang.seatdang.admin.service;

import com.matdang.seatdang.admin.dto.StoreRegistRequestDto;
import com.matdang.seatdang.admin.repository.StoreAdminRepository;
import com.matdang.seatdang.object_storage.service.FileService;
import com.matdang.seatdang.store.entity.Store;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class StoreAdminService {
    private final StoreAdminRepository storeAdminRepository;
    private final FileService fileService; // File 업로드용

    @Autowired
    public StoreAdminService(StoreAdminRepository storeAdminRepository, FileService fileService) {
        this.storeAdminRepository = storeAdminRepository;
        this.fileService = fileService;
    }

    public void regist(StoreRegistRequestDto dto, MultipartFile thumbnail, List<MultipartFile> images) {
        String uploadedThumbnailUrl = fileService.uploadSingleFile(thumbnail, "store-thumbnail"); // filePath: NCP에 생성될 파일폴더명 지정
        List<String> uploadedImagesUrl = fileService.uploadFiles(images, "store-images");
        Store store = dto.toStore();
        StoreRegistRequestDto.builder()
//                .thumbnail(uploadedThumbnailUrl)
//                .images(uploadedImagesUrl)
                .build();
        storeAdminRepository.save(store);
    }

    public int findByStoreName(String storeName) {
        Store store = storeAdminRepository.findByStoreName(storeName);
        // Store 객체가 null이면 사용 가능(중복되지 않음), 그렇지 않으면 사용 중(중복됨)
        return (store == null) ? 0 : 1;
    }
}
