package com.blog.spring.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProfileDTO {

    private String name;

    private String email;

    private String password;

    private Integer removePhoto;

    private String photo;

}
