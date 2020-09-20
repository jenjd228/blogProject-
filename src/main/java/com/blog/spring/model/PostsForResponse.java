package com.blog.spring.model;

public class PostsForResponse {

    public PostsForResponse(Object[] list){
        this.list = list;
        this.count = list.length;
    }

    private Object[] list;

    private Integer count;

    public Object[] getPosts() {
        return list;
    }

    public Integer getCount() {
        return count;
    }

}
