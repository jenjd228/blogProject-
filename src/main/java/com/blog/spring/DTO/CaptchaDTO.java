package com.blog.spring.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CaptchaDTO {

    private String secret;

    public String image;

}
