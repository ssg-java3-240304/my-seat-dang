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
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class StoreAdminController {
    private final FileService fileService;
    private final StoreAdminService storeAdminService;

    @GetMapping("/storeRegist")
    public void storeRegist(){
        log.info("GET /admin/storeRegist");
    }

    @GetMapping(path = "/storeNameCheck", produces = "application/json; charset=utf-8")
    @ResponseBody
    public int storeNameCheck(@RequestParam String storeName){
        log.info("GET /admin/storeNameCheck");
        return storeAdminService.findByStoreName(storeName);
    }

    @PostMapping("/storeRegist")
    public String storeRegist(
            @RequestParam("storeName") String storeName,
            @RequestParam("category") String category,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "notice", required = false) String notice,
            @RequestParam("phone") String phone,
            @RequestParam("thumbnail") MultipartFile thumbnail,
            @RequestParam("images") List<MultipartFile> images,
            @RequestParam("storeAddress") String storeAddress,
            @RequestParam("openTime") LocalTime openTime,
            @RequestParam("closeTime") LocalTime closeTime,
            @RequestParam(value = "startBreakTime", required = false) LocalTime startBreakTime,
            @RequestParam(value = "endBreakTime", required = false) LocalTime endBreakTime,
            @RequestParam(value = "lastOrder", required = false) LocalTime lastOrder,
            @RequestParam("regularDayOff") String regularDayOff,
            @ModelAttribute StoreRegistRequestDto dto,
            RedirectAttributes redirectAttributes) throws IOException {
        log.debug("dto = {}", dto);
        Store store = dto.toStore();
        StoreRegistRequestDto.builder()
                .storeName(storeName)
                .storeType(StoreType.valueOf(category))
                .description(description)
                .notice(notice)
                .phone(phone)
                .storeAddress(storeAddress)
                .openTime(openTime)
                .closeTime(closeTime)
                .startBreakTime(startBreakTime)
                .endBreakTime(endBreakTime)
                .lastOrder(lastOrder)
                .regularDayOff(regularDayOff)
                .build();

        storeAdminService.regist(dto, thumbnail, images);
//        redirectAttributes.addFlashAttribute("message", "메뉴를 등록했습니다.");
        return "redirect:/store/storeRegist";
    }
}
