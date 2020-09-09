package com.blog.spring.DTO;

import com.blog.spring.ModerationStatus;
import com.blog.spring.model.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostsDTO  {

    //interface GetApiPosts {}

    private Integer id;

    private Long timestamp;

    private UsersDTO user;

    private String title;

    private String announce;

    private Integer likeCount;

    private Integer dislikeCount;

    private Integer commentCount;

    private Integer viewCount;

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp.toEpochSecond(ZoneOffset.UTC);
    }

}
