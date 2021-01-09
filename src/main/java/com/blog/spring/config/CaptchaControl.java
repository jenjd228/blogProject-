package com.blog.spring.config;

import com.blog.spring.controller.ApiAuthController;
import com.blog.spring.service.AuthService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class CaptchaControl {

    private final Logger logger = Logger.getLogger(CaptchaControl.class);

    @Autowired
    private AuthService authService;

    @Scheduled(fixedRate = 3600000)
    public void scheduleFixedRateTask() {

        logger.info("Старая капча удалена");

        authService.deleteOldCaptcha();
    }

}
