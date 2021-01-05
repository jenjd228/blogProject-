package com.blog.spring.controller;

import com.blog.spring.DTO.CalendarDTO;
import com.blog.spring.DTO.CaptchaDTO;
import com.blog.spring.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiAuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/api/auth/captcha")
    public CaptchaDTO captcha(){
        return authService.getCaptcha();
    }
}
