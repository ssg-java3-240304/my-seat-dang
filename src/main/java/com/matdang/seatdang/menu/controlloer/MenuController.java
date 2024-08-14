package com.matdang.seatdang.menu.controlloer;

import com.matdang.seatdang.auth.principal.StoreOwnerUserDetails;
import com.matdang.seatdang.menu.dto.MenuRequestDto;
import com.matdang.seatdang.menu.service.MenuCommandService;
import com.matdang.seatdang.object_storage.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/storeowner/menu")
@RequiredArgsConstructor
public class MenuController {
    private final FileService fileService;
    private final MenuCommandService menuCommandService;

    @GetMapping("/regist")
    public String regist(Authentication authentication, @AuthenticationPrincipal StoreOwnerUserDetails principal) {
        log.debug("Get menu/regist");
        log.debug("Authentication: {}", authentication);
        log.debug("Principal: {}", principal);

        return "storeowner/menu/menuRegist";
    }

    @PostMapping("/regist")
    public String registMenu(@ModelAttribute("menu") MenuRequestDto menuRequestDto
            , @RequestPart(value = "files") List<MultipartFile> multipartFiles
            , Model model
            , Authentication authentication){
        log.debug("Post menu/regist, menuRequestDto: {}", menuRequestDto);
        String folderName = "menu";
        List<String> uploadedFileUrls = fileService.uploadFiles(multipartFiles, folderName);
        if(!uploadedFileUrls.isEmpty()){
            log.debug("Uploaded file urls: {}", uploadedFileUrls);
            menuRequestDto.setImage(uploadedFileUrls.get(0));
        }
        log.debug("Authentication: {}", authentication);
//        menuCommandService.regist(menuRequestDto);
        // 업로드한 파일들의 URL 리스트를 반환
        return "redirect:/storeowner/menu/regist";
    }
}
