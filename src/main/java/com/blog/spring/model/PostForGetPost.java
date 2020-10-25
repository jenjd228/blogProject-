package com.blog.spring.model;

import com.blog.spring.DTO.CommentDTO;
import com.blog.spring.DTO.UsersDTO;

import java.util.List;

public class PostForGetPost {

    private Integer id;

    private Long timestamp;

    private boolean active;

    private UsersDTO user;

    private String title;

    private String text;

    private Integer likeCount;

    private Integer dislikeCount;

    private Integer viewCount;

    private Integer commentCount;

    private List<CommentDTO> commentList;

    private  List<String> tags;

}
