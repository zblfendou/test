package com.casClient.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/casClient")
public class CasClientController {
    @RequestMapping("/test.do")
    public void test() {
        System.out.println("test pass");
    }
}
