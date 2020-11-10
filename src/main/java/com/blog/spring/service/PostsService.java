package com.blog.spring.service;

import com.blog.spring.DTO.PostsDTO;
import com.blog.spring.DTO.TagForTagsDTO;
import com.blog.spring.model.PostForGetByIdPost;
import com.blog.spring.model.Posts;
import com.blog.spring.model.Tag2post;
import com.blog.spring.model.Tags;
import com.blog.spring.repository.PostsRepository;
import com.blog.spring.repository.TagsRepository;
import org.dom4j.rule.Mode;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

import static java.util.stream.Collectors.reducing;
import static java.util.stream.Collectors.toList;

@Service
public class PostsService {

    @Qualifier("modelMapperToPostsDTO")
    private final ModelMapper modelMapperToPostsDTO;

    @Qualifier("modelMapperForByIdPost")
    private final ModelMapper modelMapperForByIdPost;

    @Qualifier("modelMapperToTagForTagsDTO")
    private final ModelMapper modelMapperToTagForTagsDTO;

    private final PostsRepository postsRepository;

    private final TagsRepository tagsRepository;

    public PostsService(ModelMapper modelMapperToTagForTagsDTO,ModelMapper modelMapperToPostsDTO,ModelMapper modelMapperForByIdPost, PostsRepository postsRepository, TagsRepository tagsRepository){
        this.modelMapperToTagForTagsDTO = modelMapperToTagForTagsDTO;
        this.modelMapperForByIdPost = modelMapperForByIdPost;
        this.modelMapperToPostsDTO = modelMapperToPostsDTO;
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

    public PostForGetByIdPost getPostById(Integer id){
        Posts post = postsRepository.findPostsById(id);
        //postForGetByIdPost = Objects.isNull(post) ? null : modelMapperForByIdPost.map(post, PostForGetByIdPost.class);
        return Objects.isNull(post) ? null : modelMapperForByIdPost.map(post, PostForGetByIdPost.class);
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
        return Objects.isNull(post) ? null : modelMapperToPostsDTO.map(post, PostsDTO.class);
    }

    public List<TagForTagsDTO> findTagsByQuery(String query){
        List<TagForTagsDTO> tags;
        Long postCount =  postsRepository.count();
        List<Tags> tags1 = (ArrayList<Tags>) tagsRepository.findAll();

        if (query.isEmpty()){
            tags = (tags1).stream().map(this::convertToTagForTagsDTO).collect(toList());
        }else {
            tags = tagsRepository.findTagsByQuery(query).stream().map(this::convertToTagForTagsDTO).collect(toList());
        }
        /**
         * try обычные веса в маппере
         * потом сорт по весу
         * потом в норм вид
         * **/
        return tags;
    }

    private TagForTagsDTO convertToTagForTagsDTO(Tags tag) {
        return Objects.isNull(tag) ? null : modelMapperToTagForTagsDTO.map(tag, TagForTagsDTO.class);
    }
}
