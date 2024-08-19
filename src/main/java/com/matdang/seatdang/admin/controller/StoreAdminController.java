package com.matdang.seatdang.admin.controller;

import com.matdang.seatdang.admin.dto.StoreDetailDto;
import com.matdang.seatdang.admin.dto.StoreRegistRequestDto;
import com.matdang.seatdang.admin.dto.StoreUpdateRequestDto;
import com.matdang.seatdang.admin.service.StoreAdminService;
import com.matdang.seatdang.auth.principal.StoreOwnerUserDetails;
import com.matdang.seatdang.menu.dto.MenuDto;
import com.matdang.seatdang.object_storage.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/storeowner")
@RequiredArgsConstructor
@Slf4j
public class StoreAdminController {
    private final FileService fileService;
    private final StoreAdminService storeAdminService;

    @GetMapping("/storeDetail")
    public void storeDetail(@RequestParam("storeId") Long storeId, Model model){
        StoreDetailDto store = storeAdminService.findByStoreId(storeId);
        log.debug("store = {}", store);
        model.addAttribute("store", store);
    }

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

    @GetMapping(path = "/storeUpdate")
    public void storeUpdate(Model model){
        StoreOwnerUserDetails userDetails = (StoreOwnerUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long storeId = userDetails.getStore().getStoreId();
        StoreDetailDto dto = storeAdminService.findByStoreId(storeId);
        log.info("GET /storeowner/storeUpdate");
        log.debug("dto = {}", dto);
        model.addAttribute("store", dto);
    }

    @PostMapping("/storeUpdate")
    public String storeUpdate(
            @ModelAttribute StoreUpdateRequestDto dto){
        storeAdminService.update(dto);
        return "redirect:/storeowner/storeUpdate";
    }
}
