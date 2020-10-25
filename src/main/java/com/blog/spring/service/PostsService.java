package com.blog.spring.service;

import com.blog.spring.DTO.PostsDTO;
import com.blog.spring.model.PostForGetPost;
import com.blog.spring.model.Posts;
import com.blog.spring.model.Tag2post;
import com.blog.spring.model.Tags;
import com.blog.spring.repository.PostsRepository;
import com.blog.spring.repository.TagsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Service
public class PostsService {

    private final ModelMapper modelMapper;

    private final PostsRepository postsRepository;

    private final TagsRepository tagsRepository;

    public PostsService(ModelMapper modelMapper, PostsRepository postsRepository, TagsRepository tagsRepository){
        this.modelMapper = modelMapper;
        this.postsRepository = postsRepository;
        this.tagsRepository = tagsRepository;

    }

    public List<PostsDTO> getPosts(Integer offset, Integer limit, String mode){
        PageRequest pageable;
        List<Posts> list;
        switch (mode) {
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
            default -> {pageable = PageRequest.of(offset / limit, limit, Sort.by("time").descending());
                list = postsRepository.findBy(pageable);}
        }
        return list.stream().map(this::convertToDto).collect(toList());
    }

    public Set<Tags> getPostById(Integer id){
        Posts post = postsRepository.findPostsById(id);
        if (post==null){
            return null;
        }
        return post.getTags();
    }

    public List<PostsDTO> getPostBySearch(Integer offset, Integer limit, String search){
        if (search.isEmpty()){
            return getPosts(offset,limit,"recent");
        }
        PageRequest pageable = PageRequest.of(offset / limit, limit);
        List<Posts> list = new ArrayList<>(postsRepository.findByQuery(pageable, LocalDateTime.now().toEpochSecond(ZoneOffset.UTC), search));
        return list.stream().map(this::convertToDto).collect(toList());
    }

    public List<PostsDTO> getPostByDate(Integer offset, Integer limit, String date){
        Long currentDate = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli();
        LocalDate dateForFind = LocalDate.parse(date);
        Long dateForFind1 = dateForFind.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
        System.out.println(currentDate);
        Long dateForFind2 = dateForFind.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();

        Pageable pageable = PageRequest.of(offset / limit, limit);
        List<Posts> list = new ArrayList<>(postsRepository.findByDate(pageable, currentDate, dateForFind1, dateForFind2));
        return list.stream().map(this::convertToDto).collect(toList());
    }

    public List<PostsDTO> getPostsByTag(Integer offset, Integer limit, String tagFromFront){
        Pageable pageable = PageRequest.of(offset / limit, limit);
        Tags tag = tagsRepository.findTagByName(tagFromFront);

        if (tag==null){
            return new ArrayList<>();
        }
        List<Tag2post> tag2posts = tag.getTag2posts();

        List<Integer> postIds = new ArrayList<>();
        tag2posts.forEach(tag2post -> postIds.add(tag2post.getPostId()));

        List<Posts> list = postsRepository.findByIds(pageable,LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli(),postIds);
        return list.stream().map(this::convertToDto).collect(toList());
    }

    private PostsDTO convertToDto(Posts post) {
        return Objects.isNull(post) ? null : modelMapper.map(post, PostsDTO.class);
    }
}
