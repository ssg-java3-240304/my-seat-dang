package com.matdang.seatdang.customer.search.controller;

import com.matdang.seatdang.customer.search.service.MapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("search")
@RequiredArgsConstructor
public class SearchController {
    private final MapService mapService;

    @GetMapping
    public String index(Model model) {
//        model.addAttribute("NcpAccessId", mapService.getAccessId());
        return "customer/search/search";
    }
}
