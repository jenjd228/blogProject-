package com.blog.spring.service;

import com.blog.spring.DTO.PostsDTO;
import com.blog.spring.model.Posts;
import com.blog.spring.repository.PostsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class PostsService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PostsRepository postsRepository;

    public Object[]  getPosts(Integer offset,Integer limit,String mode){
        PageRequest pageable;
        List<Posts> list;
        switch (mode) {
            case "recent": {
                pageable = PageRequest.of(offset, limit, Sort.by("time").ascending());
                list = postsRepository.findBy(pageable);
                break;
            }
            case "popular":{
                pageable = PageRequest.of(offset, limit);
                list = postsRepository.findByCommentCount(pageable, LocalDateTime.now());
                break;
            }
            case "best": {
                pageable = PageRequest.of(offset, limit);
                list = postsRepository.findByLikeVotes(pageable, LocalDateTime.now());
                break;
            }
            case "early": {
                pageable = PageRequest.of(offset, limit, Sort.by("time").descending());
                list = postsRepository.findBy(pageable);
                break;
            }
            default: list = new ArrayList<>();
        }
        return Objects.requireNonNull(list).stream().map(this::convertToDto).toArray();
    }

    private PostsDTO convertToDto(Posts post) {
        return Objects.isNull(post) ? null : modelMapper.map(post, PostsDTO.class);
    }
}
