package com.example.login.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestContrioller {
    @PostMapping("/test")
    public String test(){
        return "success";
    }
}
