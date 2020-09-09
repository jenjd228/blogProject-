package com.blog.spring.controller;
import com.blog.spring.DTO.PostsDTO;
import com.blog.spring.model.Posts;
import com.blog.spring.repository.PostCommentsRepository;
import com.blog.spring.repository.PostVotersRepository;
import com.blog.spring.repository.PostsRepository;
import net.minidev.json.JSONObject;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
public class ApiGeneralController {

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    PostCommentsRepository postCommentsRepository;

    @Autowired
    PostVotersRepository postVotersRepository;



    @GetMapping("api/init/")
    public JSONObject init() {
        JSONObject json = new JSONObject();
        json.put("title","DevPub");
        json.put("subtitle","Рассказы разработчиков");
        json.put("phone","+7 903 666-44-55");
        json.put("email","mail@mail.ru");
        json.put("copyright","Дмитрий Сергеев");
        json.put("copyrightFrom","2005");
        return json;
    }

    @GetMapping("/api/post/{offset}/{limit}/{mode}")
    public ResponseEntity<PostsDTO> getPosts(@PathVariable Integer offset, @PathVariable Integer limit, @PathVariable String mode) {
        JSONObject json = new JSONObject();
        List<Posts> list = new ArrayList<>();
        PageRequest pageable = null;
            switch (mode) {
                case "recent": {
                    pageable = PageRequest.of(offset, limit, Sort.by("time").ascending());
                    break;
                }
                case "popular": {
                    pageable = PageRequest.of(offset, limit, Sort.Direction.ASC, "popular");
                    break;
                }
                case "best": {
                    pageable = PageRequest.of(offset, limit, Sort.Direction.ASC, "best");
                    break;
                }
                case "early": {
                    pageable = PageRequest.of(offset, limit, Sort.by("time").descending());
                    break;
                }
            }
        if (pageable==null){
            json.put("count",0);
            json.put("posts",list.toArray());
            return new ResponseEntity(json, HttpStatus.OK);
        }
        list = postsRepository.findBy(pageable);
        json.put("count",list.size());
        json.put("posts",list.stream().map(this::convertToDto)
                .collect(Collectors.toList()).toArray());
        //json.put("timestamp", LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        return new ResponseEntity(json, HttpStatus.OK);
    }

    private PostsDTO convertToDto(Posts post) {
        PostsDTO postsDTO =  Objects.isNull(post) ? null : modelMapper.map(post, PostsDTO.class);;
        postsDTO.setCommentCount(postCommentsRepository.getTotalCount(post.getId()));
        postsDTO.setLikeCount(postVotersRepository.getLikeOrDislikeCount(post.getId(),1));
        postsDTO.setDislikeCount(postVotersRepository.getLikeOrDislikeCount(post.getId(),-1));
        return postsDTO;
    }


}
