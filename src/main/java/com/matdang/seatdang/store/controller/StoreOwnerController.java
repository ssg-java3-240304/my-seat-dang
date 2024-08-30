package com.matdang.seatdang.store.controller;

import com.matdang.seatdang.store.dto.StoreDetailDto;
import com.matdang.seatdang.store.dto.StoreRegistRequestDto;
import com.matdang.seatdang.store.dto.StoreUpdateRequestDto;
import com.matdang.seatdang.store.service.StoreOwnerService;
import com.matdang.seatdang.auth.principal.StoreOwnerUserDetails;
import com.matdang.seatdang.object_storage.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/store-owner/store")
@RequiredArgsConstructor
@Slf4j
public class StoreOwnerController {
    private final FileService fileService;
    private final StoreOwnerService storeOwnerService;

    @GetMapping("/store-detail")
    public void storeDetail(@RequestParam("storeId") Long storeId, Model model){
        StoreDetailDto store = storeOwnerService.findByStoreId(storeId);
        log.debug("store = {}", store);
        model.addAttribute("store", store);
    }

    @GetMapping("/store-regist")
    public String storeRegist(){
        log.info("GET /store/store-regist");
        return "storeowner/store/store-regist";
    }

    @GetMapping(path = "/store-name-check", produces = "application/json; charset=utf-8")
    @ResponseBody
    public int storeNameCheck(@RequestParam String storeName){
        log.info("GET /store/store-name-check");
        return storeOwnerService.findByStoreName(storeName);
    }

    @PostMapping("/store-regist")
    public String storeRegist(
            @ModelAttribute StoreRegistRequestDto dto,
            @RequestParam("thumbnail") MultipartFile thumbnail, @RequestParam("images") List<MultipartFile> images) throws IOException {
        log.debug("dto = {}", dto);

        storeOwnerService.regist(dto, thumbnail, images);
        return "redirect:/store-owner/store/store-regist";
    }

    @GetMapping(path = "/store-update")
    public String storeUpdate(Model model){
        StoreOwnerUserDetails userDetails = (StoreOwnerUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long storeId = userDetails.getStore().getStoreId();
        StoreDetailDto dto = storeOwnerService.findByStoreId(storeId);
        log.info("GET /store/store-update");
        log.debug("dto = {}", dto);
        model.addAttribute("store", dto);
        return "storeowner/store/store-update";
    }

    @PostMapping("/store-update")
    public String storeUpdate(
            @ModelAttribute StoreUpdateRequestDto dto,
            @RequestParam(value = "originalThumbnail", required = false) String originalThumbnail,
            @RequestParam(value = "originalImages", required = false) List<String> originalImages){
        log.debug("originalThumbnail = {}", originalThumbnail);
        log.debug("originalImages = {}", originalImages);
        storeOwnerService.update(dto, originalThumbnail, originalImages);
        return "redirect:/store-owner/store/store-update";
    }
}
