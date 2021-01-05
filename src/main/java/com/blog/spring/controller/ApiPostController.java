package com.blog.spring.controller;

import com.blog.spring.model.PostForGetByIdPost;
import com.blog.spring.model.PostsForResponse;
import com.blog.spring.repository.PostsRepository;
import com.blog.spring.service.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/post")
public class ApiPostController {

    private final PostsService postsService;

    private final PostsRepository postsRepository;

    ApiPostController(PostsService postsService,PostsRepository postsRepository){
        this.postsRepository = postsRepository;
        this.postsService = postsService;
    }

    @GetMapping(value = "")
    public ResponseEntity<PostsForResponse> getPosts(@RequestParam Integer offset, @RequestParam Integer limit, @RequestParam String mode) {
        return new ResponseEntity(postsService.getPosts(offset, limit, mode), HttpStatus.OK);
    }

    @GetMapping("/search/")
    public ResponseEntity<PostsForResponse> getPostBySearch(@RequestParam Integer offset, @RequestParam Integer limit, @RequestParam String search){
        return new ResponseEntity(postsService.getPostBySearch(offset, limit, search),HttpStatus.OK);
    }

    @GetMapping("/byDate")
    public ResponseEntity<PostsForResponse> getPostByDate(@RequestParam Integer offset, @RequestParam Integer limit, @RequestParam String date){
        return new ResponseEntity(postsService.getPostByDate(offset, limit, date), HttpStatus.OK);
    }

    @GetMapping("/byTag")
    public ResponseEntity<PostsForResponse> getPostByTag(@RequestParam Integer offset, @RequestParam Integer limit, @RequestParam String tag){
        return new ResponseEntity(postsService.getPostsByTag(offset,limit,tag), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostsForResponse> getPost(@PathVariable Integer id){
        PostForGetByIdPost post = postsService.getPostById(id);
        if (post==null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(post, HttpStatus.OK);
    }

}
