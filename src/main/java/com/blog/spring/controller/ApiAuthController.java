package com.blog.spring.controller;

import com.blog.spring.DTO.CaptchaDTO;
import com.blog.spring.DTO.EmailOnlyDTO;
import com.blog.spring.DTO.EmailPasswordUserDTO;
import com.blog.spring.DTO.RegisterDTO;
import com.blog.spring.service.AuthService;
import net.minidev.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;

@RestController
@RequestMapping(value = "/api/auth/")
public class ApiAuthController {

    private final Logger logger = Logger.getLogger(ApiAuthController.class);

    @Autowired
    private AuthService authService;

    @GetMapping("captcha")
    public CaptchaDTO captcha() {
        logger.info("/api/auth/captcha - Была запрошена капча");
        return authService.getCaptcha();
    }

    @PostMapping("register")
    public JSONObject register(@RequestBody RegisterDTO registerDTO) {
        logger.info("/api/auth/register - Регистрация нового пользователя"+registerDTO.toString());
        return authService.register(registerDTO.getE_mail(), registerDTO.getPassword()
                , registerDTO.getName()
                , registerDTO.getCaptcha()
                , registerDTO.getCaptcha_secret());
    }

    @PostMapping("restore")
    public JSONObject restore(@RequestBody EmailOnlyDTO emailOnlyDTO) {
        logger.info("/api/auth/restore - Отправка ссылки на " + emailOnlyDTO.getEmail() + " для изменения пароля");
        return authService.restore(emailOnlyDTO.getEmail());
    }

    @PostMapping("password")
    public JSONObject password(@RequestParam String code, @RequestParam String password,
                               @RequestParam String captcha,
                               @RequestParam String captchaSecret) {
        logger.info("/api/auth/password - Запрос на изменение пароля");
        return authService.password(code, password, captcha, captchaSecret);
    }

    @PostMapping("login")
    public JSONObject login(@RequestBody EmailPasswordUserDTO emailPasswordUserDTO) {
        logger.info("/api/auth/login - Запрос на авторизацию пользователя с email = " + emailPasswordUserDTO.getE_mail());
        return authService.login(emailPasswordUserDTO.getE_mail(), emailPasswordUserDTO.getPassword());
    }

    @GetMapping("check")
    public JSONObject check() {
        logger.info("/api/auth/check - Запрос на аунтефикацию текущего пользователя ");
        return authService.check();
    }

    @GetMapping("logout")
    public JSONObject logout(){
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        logger.info("/api/auth/logout Разлогинивание пользователя с сессией : "+sessionId);
        return authService.logout(sessionId);
    }
}
