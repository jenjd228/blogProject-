package com.blog.spring.controller;

import com.blog.spring.DTO.*;
import com.blog.spring.model.ModerationStatus;
import com.blog.spring.model.PostForGetByIdPost;
import com.blog.spring.model.PostsForResponse;
import com.blog.spring.repository.PostsRepository;
import com.blog.spring.service.AuthService;
import com.blog.spring.service.PostsService;
import net.minidev.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;

@RestController
@RequestMapping(value = "/api/post")
public class ApiPostController {

    private final Logger logger = Logger.getLogger(ApiPostController.class);

    private final PostsService postsService;

    private final AuthService authService;

    ApiPostController(AuthService authService,PostsService postsService) {
        this.postsService = postsService;
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<PostsForResponse> getPosts(@RequestParam Integer offset, @RequestParam Integer limit, @RequestParam String mode) {
        logger.info("/api/post - Запрос данных постов с mode = " + mode);
        return new ResponseEntity<>(postsService.getPosts(offset, limit, mode), HttpStatus.OK);
    }

    @PostMapping
    public JSONObject addPost(@RequestBody AddPostDTO addPostDTO){
        logger.info("/post - Добавление поста : " + addPostDTO.toString());
        return postsService.addPost(addPostDTO);
    }

    @GetMapping("/search")
    public ResponseEntity<PostsForResponse> getPostBySearch(@RequestParam Integer offset, @RequestParam Integer limit, @RequestParam String query) {
        logger.info("/search - Запрос данных постов с поиском = " + query);
        return new ResponseEntity<>(postsService.getPostBySearch(offset, limit, query), HttpStatus.OK);
    }

    @GetMapping("/byDate")
    public ResponseEntity<PostsForResponse> getPostByDate(@RequestParam Integer offset, @RequestParam Integer limit, @RequestParam String date) {
        logger.info("/byDate - Запрос данных постов по дате : " + date);
        return new ResponseEntity<>(postsService.getPostByDate(offset, limit, date), HttpStatus.OK);
    }

    @GetMapping("/byTag")
    public ResponseEntity<PostsForResponse> getPostByTag(@RequestParam Integer offset, @RequestParam Integer limit, @RequestParam String tag) {
        logger.info("/byTag - Запрос данных постов по тегу : " + tag);
        return new ResponseEntity<>(postsService.getPostsByTag(offset, limit, tag), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostForGetByIdPost> getPost(@PathVariable Integer id) {

        logger.info("/{id} - Запрос данных поста по id : " + id);

        PostForGetByIdPost post = postsService.getPostById(id);
        if (post == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String sessionID = RequestContextHolder.currentRequestAttributes().getSessionId();

        postsService.addViewToPostIfNotModeratorAndWriter(id,sessionID);

        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @PostMapping("like")
    public JSONObject like(@RequestBody PostIdOnlyDTO postIdOnlyDTO){
        logger.info("/like - Лайк поста с id : " + postIdOnlyDTO.getPost_id());
        return postsService.like(postIdOnlyDTO.getPost_id());
    }

    @PostMapping("dislike")
    public JSONObject dislike(@RequestBody PostIdOnlyDTO postIdOnlyDTO){
        logger.info("/dislike - Дизлайк поста с id : " + postIdOnlyDTO.getPost_id());
        return postsService.dislike(postIdOnlyDTO.getPost_id());
    }

    @GetMapping("moderation")
    public ResponseEntity<PostsForResponse> getPostsForModeration(@RequestParam Integer offset, @RequestParam Integer limit, @RequestParam String status){
        logger.info("/moderation - Запрос постов на модерацию : ");
        return new ResponseEntity<>(postsService.getPostsForModeration(offset, limit, status), HttpStatus.OK);
    }

    @GetMapping("my")
    public ResponseEntity<PostsForResponse> my(@RequestParam Integer offset, @RequestParam Integer limit, @RequestParam String status){
        logger.info("/my - Запрос постов юзера со статусом : "+status);
        return new ResponseEntity<>(postsService.my(offset,limit,status),HttpStatus.OK);

    }


}
