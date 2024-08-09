package com.matdang.seatdang.menu.controlloer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class layoutController {
    @GetMapping("/admin/main")
    public void testPage1() {

    }

    @GetMapping("/storeowner/main")
    public void testPage2() {

    }

}
