package com.matdang.seatdang.admin.service;

import com.matdang.seatdang.admin.dto.StoreDetailDto;
import com.matdang.seatdang.admin.dto.StoreRegistRequestDto;
import com.matdang.seatdang.admin.dto.StoreUpdateRequestDto;
import com.matdang.seatdang.admin.repository.StoreAdminRepository;
import com.matdang.seatdang.object_storage.service.FileService;
import com.matdang.seatdang.store.entity.Store;
import com.matdang.seatdang.store.repository.StoreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class StoreAdminService {
    private final StoreRepository storeRepository;
    private final StoreAdminRepository storeAdminRepository;
    private final FileService fileService; // File 업로드용

    public void regist(StoreRegistRequestDto dto, MultipartFile thumbnail, List<MultipartFile> images) {
        String uploadedThumbnailUrl = fileService.uploadSingleFile(thumbnail, "store-thumbnail"); // filePath: NCP에 생성될 파일폴더명 지정
        List<String> uploadedImagesUrl = fileService.uploadFiles(images, "store-images");
        log.debug("images ={}", uploadedImagesUrl);
        System.out.println(dto);
        Store store = Store.builder()
                .storeName(dto.getStoreName())
                .storeType(dto.getStoreType())
                .description(dto.getDescription())
                .notice(dto.getNotice())
                .phone(dto.getPhone())
                .storeAddress(dto.getStoreAddress())
                .openTime(dto.getOpenTime())
                .closeTime(dto.getCloseTime())
                .startBreakTime(dto.getStartBreakTime())
                .endBreakTime(dto.getEndBreakTime())
                .lastOrder(dto.getLastOrder())
                .regularDayOff(dto.getRegularDayOff())
                .thumbnail(uploadedThumbnailUrl)
                .images(uploadedImagesUrl)
                .build();
        storeAdminRepository.save(store);
    }

    public int findByStoreName(String storeName) {
        Store store = storeAdminRepository.findByStoreName(storeName);
        // Store 객체가 null이면 사용 가능(중복되지 않음), 그렇지 않으면 사용 중(중복됨)
        return (store == null) ? 0 : 1;
    }


    public StoreDetailDto findByStoreId(Long storeId) {
        return StoreDetailDto.fromStore(
                storeAdminRepository.findById(storeId)
                        .orElseThrow(() -> new RuntimeException("Store not found for id: " + storeId))
        );
    }

    public void update(StoreUpdateRequestDto dto) {
        String uploadedThumbnailUrl = fileService.uploadSingleFile(dto.getThumbnail(), "store-thumbnail"); // filePath: NCP에 생성될 파일폴더명 지정
        List<String> uploadedImagesUrl = fileService.uploadFiles(dto.getImages(), "store-images");
        log.debug("thumbnail ={}", uploadedThumbnailUrl);
        log.debug("images ={}", uploadedImagesUrl);

        StoreDetailDto storeDetailDto = StoreDetailDto.builder()
                .storeName(dto.getStoreName())
                .description(dto.getDescription())
                .notice(dto.getNotice())
                .phone(dto.getPhone())
                .thumbnail(uploadedThumbnailUrl)
                .images(uploadedImagesUrl)
                .storeAddress(dto.getStoreAddress())
                .openTime(dto.getOpenTime())
                .closeTime(dto.getCloseTime())
                .startBreakTime(dto.getStartBreakTime())
                .endBreakTime(dto.getEndBreakTime())
                .lastOrder(dto.getLastOrder())
                .regularDayOff(dto.getRegularDayOff())
                .build();

        if(dto.getThumbnail().getSize()>0){
            String folderName = "store-thumbnail";
            String uploadedFileUrl = fileService.uploadSingleFile(dto.getThumbnail(), folderName);
            if(!uploadedFileUrl.isEmpty()){
                log.debug("Thumbnail URL: {}", uploadedFileUrl);
                storeDetailDto.setThumbnail(uploadedFileUrl);
            }
        }

        if(!dto.getImages().isEmpty()){
            String folderName = "store-images";
            List<String> uploadedFileUrl = fileService.uploadFiles(dto.getImages(), folderName);
            if(!uploadedFileUrl.isEmpty()){
                log.debug("Images URL: {}", uploadedFileUrl);
                storeDetailDto.setImages(uploadedFileUrl);
            }
        }

        Store store = storeRepository.findByStoreId(dto.getStoreId());
        store.update(storeDetailDto);
    }
}
