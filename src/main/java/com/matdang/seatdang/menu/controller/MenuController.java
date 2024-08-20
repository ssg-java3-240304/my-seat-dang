package com.matdang.seatdang.menu.controller;

import com.matdang.seatdang.auth.principal.StoreOwnerUserDetails;
import com.matdang.seatdang.common.paging.PageCriteria;
import com.matdang.seatdang.menu.dto.MenuDto;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Controller
@RequestMapping("/storeowner/menu")
@RequiredArgsConstructor
public class MenuController {
    private final FileService fileService;
    private final MenuCommandService menuCommandService;
    private final MenuQueryService menuQueryService;

    //메뉴 목록 조회
    @GetMapping
    public String list(@PageableDefault(page = 1, size = 10) Pageable pageable, Model model) {
        log.debug("Get request menu");

        //Pageable을 0-based로 변환
        pageable = PageRequest.of(pageable.getPageNumber()-1, pageable.getPageSize());

        StoreOwnerUserDetails userDetails = (StoreOwnerUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Page<MenuDto> menuPageResponse = menuQueryService.findMenuPageByStoreIdNotDeleted(userDetails.getStore().getStoreId(), pageable);
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
            , @RequestPart(value = "file") MultipartFile multipartFile
            , Model model){
        log.debug("Post menu/regist, menuRequestDto: {}", menuRequestDto);

        //이미지 업로드 및 업로드된 url을 menuRequestDto에 세팅
        if(multipartFile.getSize()>0){
            String folderName = "menu";
            String uploadedFileUrl = fileService.uploadSingleFile(multipartFile, folderName);
            if(!uploadedFileUrl.isEmpty()){
                log.debug("Uploaded file urls: {}", uploadedFileUrl);
                menuRequestDto.setImage(uploadedFileUrl);
            }
        }

        StoreOwnerUserDetails userDetails = (StoreOwnerUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        menuRequestDto.setStoreId(userDetails.getStore().getStoreId());

        menuCommandService.regist(menuRequestDto.toMenuDto());
        return "redirect:/storeowner/menu/regist";
    }

    //메뉴 수정
    @GetMapping("/update/{menuId}")
    public String update(@PathVariable Long menuId, Model model) {
        log.debug("Get menu/update");
        log.debug("menuId: {}", menuId);
        MenuDto menuDTO = menuQueryService.findByMenuId(menuId);
        model.addAttribute("menu", menuDTO);
        return "storeowner/menu/menuUpdate";
    }

    @PostMapping("/update")
    public String updateMenu(@ModelAttribute("menu") MenuRequestDto menuRequestDto
            , @RequestPart(value = "files") MultipartFile multipartFile
            , Model model){
        log.debug("Update menu/ menuRequestDto: {}", menuRequestDto);
        log.debug("Update menu/ multipartFile: {}", multipartFile);
        //이미지 업로드 및 업로드된 url을 menuRequestDto에 세팅
        if(multipartFile.getSize()>0){
            String folderName = "menu";
            String uploadedFileUrl = fileService.uploadSingleFile(multipartFile, folderName);
            if(!uploadedFileUrl.isEmpty()){
                log.debug("Uploaded file urls: {}", uploadedFileUrl);
                menuRequestDto.setImage(uploadedFileUrl);
            }
        }
        menuCommandService.update(menuRequestDto.toMenuDto());
        log.debug("메뉴 수정 완료");
        return "redirect:/storeowner/menu";
    }

    @PostMapping("/delete")
    public String updateMenu(@RequestParam("menu_id") Long menuId, Model model){
        log.debug("delete menu/ menuId: {}", menuId);
        //이미지 업로드 및 업로드된 url을 menuRequestDto에 세팅

        menuCommandService.delete(menuId);
        log.debug("메뉴 삭제 완료");
        return "redirect:/storeowner/menu";
    }
}
