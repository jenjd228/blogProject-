package com.blog.spring.controller;

import com.blog.spring.DTO.PostsDTO;
import com.blog.spring.model.PostsForResponse;
import com.blog.spring.service.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ApiPostController {

    @Autowired
    private PostsService postsService;

    @GetMapping("/api/post")
    public ResponseEntity<PostsForResponse> getPosts(@RequestParam Integer offset, @RequestParam Integer limit, @RequestParam String mode) {
        List<PostsDTO> list = postsService.getPosts(offset, limit, mode);
        return new ResponseEntity(new PostsForResponse(list), HttpStatus.OK);
    }

    @GetMapping("/api/post/search/")
    public ResponseEntity<PostsForResponse> getPostBySearch(@RequestParam Integer offset, @RequestParam Integer limit, @RequestParam String search){
        List<PostsDTO> list = postsService.getPostBySearch(offset, limit, search);
        return new ResponseEntity(new PostsForResponse(list), HttpStatus.OK);
    }

    @GetMapping("/api/post/byDate")
    public ResponseEntity<PostsForResponse> getPostByDate(@RequestParam Integer offset, @RequestParam Integer limit, @RequestParam String date){
        List<PostsDTO> list = postsService.getPostByDate(offset, limit, date);
        return new ResponseEntity(new PostsForResponse(list), HttpStatus.OK);
    }



}
