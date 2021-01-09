package com.blog.spring.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDTO {

    private Integer id;

    private String name;

    private String photo;

    private String email;

    private boolean moderation;

    private long moderationCount;

    private boolean settings;

    public void setModerationInModelMapper(Integer moderation){
        this.moderation = moderation == 1;
    }

    public void setSettingsInModelMapper(Integer moderation){
        this.settings = moderation == 1;
    }

}
