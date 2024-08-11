package com.matdang.seatdang.admin.controller;

import com.matdang.seatdang.admin.dto.StoreRegistRequestDto;
import com.matdang.seatdang.admin.service.StoreAdminService;
import com.matdang.seatdang.common.storeEnum.StoreType;
import com.matdang.seatdang.object_storage.model.dto.FileDto;
import com.matdang.seatdang.object_storage.service.FileService;
import com.matdang.seatdang.store.entity.Store;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/storeowner")
@RequiredArgsConstructor
@Slf4j
public class StoreAdminController {
    private final FileService fileService;
    private final StoreAdminService storeAdminService;

    @GetMapping("/storeRegist")
    public void storeRegist(){
        log.info("GET /storeowner/storeRegist");
    }

    @GetMapping(path = "/storeNameCheck", produces = "application/json; charset=utf-8")
    @ResponseBody
    public int storeNameCheck(@RequestParam String storeName){
        log.info("GET /storeowner/storeNameCheck");
        return storeAdminService.findByStoreName(storeName);
    }

    @PostMapping("/storeRegist")
    public String storeRegist(
            @ModelAttribute StoreRegistRequestDto dto,
            @RequestParam("thumbnail") MultipartFile thumbnail, @RequestParam("images") List<MultipartFile> images) throws IOException {
        log.debug("dto = {}", dto);

        storeAdminService.regist(dto, thumbnail, images);
        return "redirect:/storeowner/storeRegist";
    }
}
