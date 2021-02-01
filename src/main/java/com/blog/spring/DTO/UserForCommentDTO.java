package com.blog.spring.DTO;

import lombok.Data;

@Data
public class UserForCommentDTO {

    private Integer id;

    private String name;

    private String photo;

    public void setPhotoWithDomain(String photo,String domain) {
        if (photo != null){
            this.photo = domain+"/"+photo;
        }else {
            this.photo = null;
        }
    }
}
