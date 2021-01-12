package com.blog.spring.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDTO {

    private String e_mail;

    private String password;

    private String name;

    private String captcha;

    private String captcha_secret;

}