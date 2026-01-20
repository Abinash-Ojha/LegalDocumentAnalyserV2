package com.legaldocAnalyserV2.Test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/secure")
    public String secure() {
        return "JWT is working";
    }
}
