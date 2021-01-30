package com.blog.spring.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordDTO {

    private String code;

    private String password;

    private String captcha;

    @JsonProperty("captcha_secret")
    private String captchaSecret;

}
