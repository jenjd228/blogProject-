package com.blog.spring.model;

import com.blog.spring.DTO.CommentDTO;
import com.blog.spring.DTO.UsersDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostForGetByIdPost {

    private Integer id;

    private Long timestamp;

    private boolean active;

    private UsersDTO user;

    private String title;

    private String text;

    private Integer likeCount;

    private Integer dislikeCount;

    private Integer viewCount;

    private List<CommentDTO> comments;

    private  List<String> tags;

    public void setActive(Integer active) {
        this.active = active == 1;
    }

    public void setLikeCount(List<PostVotes> list){ this.likeCount = list.size(); }

    public void setDislikeCount(List<PostVotes> list){ this.dislikeCount = list.size(); }

    public void setTags(List<Tags> tags){
        if (this.tags == null){
            this.tags = new ArrayList<>();
        }
        for (Tags tag: tags){
            this.tags.add(tag.getName());
        }
    }
}
