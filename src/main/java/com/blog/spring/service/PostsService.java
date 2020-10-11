package com.blog.spring.service;

import com.blog.spring.DTO.PostsDTO;
import com.blog.spring.model.Posts;
import com.blog.spring.repository.PostsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Service
public class PostsService {

    private final ModelMapper modelMapper;

    private final PostsRepository postsRepository;


    public PostsService(ModelMapper modelMapper, PostsRepository postsRepository){
        this.modelMapper = modelMapper;
        this.postsRepository = postsRepository;
    }

    public List<PostsDTO>  getPosts(Integer offset, Integer limit, String mode){
        PageRequest pageable;
        List<Posts> list = new ArrayList<>();
        switch (mode) {
            case "recent" -> {
                pageable = PageRequest.of(offset / limit, limit, Sort.by("time").descending());
                list = postsRepository.findBy(pageable);
            }
            case "popular" -> {
                pageable = PageRequest.of(offset / limit, limit);
                list = postsRepository.findByCommentCount(pageable, LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
            }
            case "best" -> {
                pageable = PageRequest.of(offset / limit, limit);
                list = postsRepository.findByLikeVotes(pageable, LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
            }
            case "early" -> {
                pageable = PageRequest.of(offset / limit, limit, Sort.by("time").ascending());
                list = postsRepository.findBy(pageable);
            }
        }
        return list.stream().map(this::convertToDto).collect(toList());
    }

    public List<PostsDTO> getPostBySearch(Integer offset, Integer limit, String search){
        PageRequest pageable = PageRequest.of(offset / limit, limit);
        List<Posts> list = new ArrayList<>(postsRepository.findByQuery(pageable, LocalDateTime.now().toEpochSecond(ZoneOffset.UTC), search));
        return list.stream().map(this::convertToDto).collect(toList());
    }

    public List<PostsDTO> getPostByDate(Integer offset, Integer limit, String date){
        LocalDateTime date1 = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String text = date1.format(formatter);

        String[] strings = text.split("-");
        String[] strings1 = date.split("-");

        Long currentDate = LocalDateTime.of(Integer.parseInt(strings[0]),Integer.parseInt(strings[1]),Integer.parseInt(strings[2]),0,0).toEpochSecond(ZoneOffset.UTC);
        Long dateForFind = LocalDateTime.of(Integer.parseInt(strings1[0]),Integer.parseInt(strings1[1]),Integer.parseInt(strings1[2]),0,0).toEpochSecond(ZoneOffset.UTC);
        System.out.println(dateForFind);
        PageRequest pageable = PageRequest.of(offset / limit, limit);
        List<Posts> list = new ArrayList<>(postsRepository.findByDate(pageable, currentDate, dateForFind));
        return list.stream().map(this::convertToDto).collect(toList());
    }

    private PostsDTO convertToDto(Posts post) {
        return Objects.isNull(post) ? null : modelMapper.map(post, PostsDTO.class);
    }
}
