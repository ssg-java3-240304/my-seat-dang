package com.matdang.seatdang;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
public class AppController {

    @Value("${app.version}")
    private String appVersion;

//    @GetMapping("/")
//    @ResponseBody
//    public String index() {
//        log.info("GET / : appVersion = {}", appVersion);
//        return """
//            <h1>🥖🥪🌯🌮맛있는 음식을 눈 앞에🍰🎂🧁🍮🍯</h1>
//            <h1>🥐🍞🥨🥯마싯당 입니다🥟🍘🥮</h1>
//            <h4>version %s</h4>
//            """.formatted(appVersion);
//    }

//    @GetMapping(path = "/", produces = MediaType.TEXT_HTML_VALUE)
//    public ResponseEntity<String> index() {
//        log.info("GET / : appVersion = {}", appVersion);
//        String responseBody = String.format("""
//        <h1>🥖🥪🌯🌮맛있는 음식을 눈 앞에🍰🎂🧁🍮🍯</h1>
//        <h1>🥐🍞🥨🥯마싯당 입니다🥟🍘🥮</h1>
//        <h4>version %s</h4>
//        """, appVersion);
//        return ResponseEntity.ok(responseBody);
//    }

}