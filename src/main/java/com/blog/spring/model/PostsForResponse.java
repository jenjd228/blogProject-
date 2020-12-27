package com.blog.spring.model;

import com.blog.spring.DTO.PostsDTO;

import java.util.List;

public class PostsForResponse {

    private List<PostsDTO> list;

    private long count;

    public PostsForResponse(List<PostsDTO> list, long postCount){
        this.list = list;
        this.count = postCount ;
    }

    public List<PostsDTO> getPosts() {
        return list;
    }

    public long getCount() {
        return count;
    }

}
