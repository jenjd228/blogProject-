package com.blog.spring.controller;

import com.blog.spring.DTO.EmailOnlyDTO;
import com.blog.spring.DTO.EmailPasswordUserDTO;
import com.blog.spring.DTO.PasswordDTO;
import com.blog.spring.DTO.RegisterDTO;
import com.blog.spring.service.AuthService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/auth/")
public class ApiAuthController {

    private final Logger logger = Logger.getLogger(ApiAuthController.class);

    @Autowired
    private AuthService authService;

    @GetMapping("captcha")
    public ResponseEntity captcha() {
        logger.info("/api/auth/captcha - Была запрошена капча");
        return new ResponseEntity(authService.getCaptcha(), HttpStatus.OK);
    }

    @PostMapping("register")
    public ResponseEntity register(@RequestBody RegisterDTO registerDTO) {
        logger.info("/api/auth/register - Регистрация нового пользователя" + registerDTO.toString());
        return new ResponseEntity(authService.register(registerDTO), HttpStatus.OK);
    }

    @PostMapping("restore")
    public ResponseEntity restore(@RequestBody EmailOnlyDTO emailOnlyDTO) {
        logger.info("/api/auth/restore - Отправка ссылки на " + emailOnlyDTO.getEmail() + " для изменения пароля");
        return new ResponseEntity(authService.restore(emailOnlyDTO.getEmail()), HttpStatus.OK);
    }

    @PostMapping("password")
    public ResponseEntity password(@RequestBody PasswordDTO passwordDTO) {
        logger.info("/api/auth/password - Запрос на изменение пароля");
        return new ResponseEntity(authService.password(passwordDTO), HttpStatus.OK);
    }

    @PostMapping("login")
    public ResponseEntity login(@RequestBody EmailPasswordUserDTO emailPasswordUserDTO) {
        logger.info("/api/auth/login - Запрос на авторизацию пользователя с email = " + emailPasswordUserDTO.getEmail());
        return new ResponseEntity(authService.login(emailPasswordUserDTO.getEmail(), emailPasswordUserDTO.getPassword()), HttpStatus.OK);
    }

    @GetMapping("check")
    public ResponseEntity check() {
        logger.info("/api/auth/check - Запрос на аунтефикацию текущего пользователя ");
        return new ResponseEntity(authService.check(), HttpStatus.OK);
    }

    @GetMapping("logout")
    public ResponseEntity logout() {
        logger.info("/api/auth/logout Разлогинивание пользователя с сессией ");
        return new ResponseEntity(authService.logout(), HttpStatus.OK);
    }
}
