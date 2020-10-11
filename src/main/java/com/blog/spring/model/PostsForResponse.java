package com.blog.spring.model;

import com.blog.spring.DTO.PostsDTO;

import java.util.List;

public class PostsForResponse {

    private List<PostsDTO> list;

    private Integer count;

    public PostsForResponse(List<PostsDTO> list){
        this.list = list;
        this.count = list.size();
    }

    public List<PostsDTO> getPosts() {
        return list;
    }

    public Integer getCount() {
        return count;
    }

}
