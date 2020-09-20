package com.blog.spring.controller;

import com.blog.spring.model.PostsForResponse;
import com.blog.spring.repository.PostsRepository;
import com.blog.spring.service.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiPostController {

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private PostsService postsService;

    @GetMapping("/api/post")
    public ResponseEntity<PostsForResponse> getPosts(@RequestParam Integer offset, @RequestParam Integer limit, @RequestParam String mode) {
        Object[] list = postsService.getPosts(offset,limit,mode);
        return new ResponseEntity(new PostsForResponse(list), HttpStatus.OK);
    }

}
