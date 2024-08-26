package com.matdang.seatdang.store.controller;

import com.matdang.seatdang.store.dto.StoreDetailDto;
import com.matdang.seatdang.store.dto.StoreRegistRequestDto;
import com.matdang.seatdang.store.dto.StoreUpdateRequestDto;
import com.matdang.seatdang.store.service.StoreAdminService;
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
@RequestMapping("/storeowner")
@RequiredArgsConstructor
@Slf4j
public class StoreOwnerController {
    private final FileService fileService;
    private final StoreAdminService storeAdminService;

    @GetMapping("/store/main")
    public String main() {

        return "store/main";
    }

    @GetMapping("/store/storeDetail")
    public void storeDetail(@RequestParam("storeId") Long storeId, Model model){
        StoreDetailDto store = storeAdminService.findByStoreId(storeId);
        log.debug("store = {}", store);
        model.addAttribute("store", store);
    }

    @GetMapping("/store/storeRegist")
    public void storeRegist(){
        log.info("GET /store/storeRegist");
    }

    @GetMapping(path = "/store/storeNameCheck", produces = "application/json; charset=utf-8")
    @ResponseBody
    public int storeNameCheck(@RequestParam String storeName){
        log.info("GET /store/storeNameCheck");
        return storeAdminService.findByStoreName(storeName);
    }

    @PostMapping("/store/storeRegist")
    public String storeRegist(
            @ModelAttribute StoreRegistRequestDto dto,
            @RequestParam("thumbnail") MultipartFile thumbnail, @RequestParam("images") List<MultipartFile> images) throws IOException {
        log.debug("dto = {}", dto);

        storeAdminService.regist(dto, thumbnail, images);
        return "redirect:/storeowner/store/storeRegist";
    }

    @GetMapping(path = "/store/storeUpdate")
    public void storeUpdate(Model model){
        StoreOwnerUserDetails userDetails = (StoreOwnerUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long storeId = userDetails.getStore().getStoreId();
        StoreDetailDto dto = storeAdminService.findByStoreId(storeId);
        log.info("GET /store/storeUpdate");
        log.debug("dto = {}", dto);
        model.addAttribute("store", dto);
    }

    @PostMapping("/store/storeUpdate")
    public String storeUpdate(
            @ModelAttribute StoreUpdateRequestDto dto){
        storeAdminService.update(dto);
        return "redirect:/storeowner/store/storeUpdate";
    }
}
