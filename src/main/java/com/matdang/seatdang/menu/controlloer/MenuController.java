package com.matdang.seatdang.menu.controlloer;

import com.matdang.seatdang.auth.principal.StoreOwnerUserDetails;
import com.matdang.seatdang.common.paging.PageCriteria;
import com.matdang.seatdang.menu.dto.MenuDetailResponseDto;
import com.matdang.seatdang.menu.dto.MenuRequestDto;
import com.matdang.seatdang.menu.service.MenuCommandService;
import com.matdang.seatdang.menu.service.MenuQueryService;
import com.matdang.seatdang.object_storage.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    private final MenuQueryService menuQueryService;

    @GetMapping
    public String list(@PageableDefault(page = 1, size = 10) Pageable pageable, Model model) {
        log.debug("Get request menu");

        //Pageable을 0-based로 변환
        pageable = PageRequest.of(pageable.getPageNumber()-1, pageable.getPageSize());

        Page<MenuDetailResponseDto> menuPageResponse = menuQueryService.findMenuPageByStoreId(1L, pageable);
        model.addAttribute("menus", menuPageResponse.getContent());

        // 페이지바 설정
        int page = menuPageResponse.getNumber(); // 0-based 페이지번호
        int limit = menuPageResponse.getSize();
        int totalCount = (int) menuPageResponse.getTotalElements();
        String url = "menu";
        model.addAttribute("pageCriteria", new PageCriteria(page, limit, totalCount, url));
        log.debug("page url = {} page = {}", url, menuPageResponse.getNumber() );

        return "storeowner/menu/menuList";
    }

    @GetMapping("/regist")
    public String regist(Model model) {
        log.debug("Get menu/regist");
        return "storeowner/menu/menuRegist";
    }

    @PostMapping("/regist")
    public String registMenu(@ModelAttribute("menu") MenuRequestDto menuRequestDto
            , @RequestPart(value = "files") List<MultipartFile> multipartFiles
            , Model model){
        log.debug("Post menu/regist, menuRequestDto: {}", menuRequestDto);
        String folderName = "menu";
        //이미지 업로드 및 업로드된 url을 menuRequestDto에 세팅
        List<String> uploadedFileUrls = fileService.uploadFiles(multipartFiles, folderName);
        if(!uploadedFileUrls.isEmpty()){
            log.debug("Uploaded file urls: {}", uploadedFileUrls);
            menuRequestDto.setImage(uploadedFileUrls.get(0));
        }
        StoreOwnerUserDetails userDetails = (StoreOwnerUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        menuRequestDto.setStoreId(1L);

        menuCommandService.regist(menuRequestDto);
        return "redirect:/storeowner/menu/regist";
    }
}
