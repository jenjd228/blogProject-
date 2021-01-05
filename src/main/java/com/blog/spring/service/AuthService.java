package com.blog.spring.service;

import com.blog.spring.DTO.CaptchaDTO;
import com.blog.spring.model.CaptchaCodes;
import com.blog.spring.repository.CaptchaCodesRepository;
import com.github.cage.Cage;
import com.github.cage.GCage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private CaptchaCodesRepository captchaCodesRepository;

    public CaptchaDTO getCaptcha() {
        Cage cage = new GCage();
        OutputStream os = null;

        String code = cage.getTokenGenerator().next();
        String secretCode = UUID.randomUUID().toString();

        try {
            os = new FileOutputStream("captcha.jpg", false);
            cage.draw(code, os);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] bytes = Objects.requireNonNull(os).toString().getBytes();

        String resultString ="data:image/png;base64, " + Base64.getEncoder().encodeToString(bytes);

        CaptchaCodes captchaCodes = new CaptchaCodes();
        captchaCodes.setCode(code);
        captchaCodes.setSecretCode(secretCode);
        captchaCodes.setTime(LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli());

        captchaCodesRepository.save(captchaCodes);

        return new CaptchaDTO(secretCode,resultString);

    }
}
