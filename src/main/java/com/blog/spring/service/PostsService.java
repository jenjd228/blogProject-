package com.blog.spring.service;

import com.blog.spring.DTO.AllStatisticsDTO;
import com.blog.spring.DTO.PostsDTO;
import com.blog.spring.DTO.StatisticDTO;
import com.blog.spring.DTO.Statistics;
import com.blog.spring.model.*;
import com.blog.spring.repository.PostVotersRepository;
import com.blog.spring.repository.PostsRepository;
import com.blog.spring.repository.Tag2PostRepository;
import com.blog.spring.repository.TagsRepository;
import net.minidev.json.JSONObject;
import org.dom4j.rule.Mode;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Service
public class PostsService {

    @Qualifier("modelMapperToPostsDTO")
    private final ModelMapper modelMapperToPostsDTO;

    @Qualifier("modelMapperForByIdPost")
    private final ModelMapper modelMapperForByIdPost;

    private final PostsRepository postsRepository;

    private final TagsRepository tagsRepository;

    private final Tag2PostRepository tag2PostRepository;

    private final AuthService authService;

    private final PostVotersRepository postVotersRepository;

    public PostsService(PostVotersRepository postVotersRepository, AuthService authService, ModelMapper modelMapperToPostsDTO, ModelMapper modelMapperForByIdPost, PostsRepository postsRepository, TagsRepository tagsRepository, Tag2PostRepository tag2PostRepository) {
        this.postVotersRepository = postVotersRepository;
        this.authService = authService;
        this.modelMapperForByIdPost = modelMapperForByIdPost;
        this.modelMapperToPostsDTO = modelMapperToPostsDTO;
        this.postsRepository = postsRepository;
        this.tagsRepository = tagsRepository;
        this.tag2PostRepository = tag2PostRepository;
    }

    public PostsForResponse getPosts(Integer offset, Integer limit, String mode) {
        Long currentDate = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli();
        PageRequest pageable;
        List<Posts> list;
        switch (mode) {
            case "popular" -> {
                pageable = PageRequest.of(offset / limit, limit);
                list = postsRepository.findByCommentCount(pageable, currentDate);
            }
            case "best" -> {
                pageable = PageRequest.of(offset / limit, limit);
                list = postsRepository.findByLikeVotes(pageable, currentDate);
            }
            case "early" -> {
                pageable = PageRequest.of(offset / limit, limit, Sort.by("time").ascending());
                list = postsRepository.findBy(pageable,currentDate);
            }
            default -> {
                pageable = PageRequest.of(offset / limit, limit, Sort.by("time").descending());
                    list = postsRepository.findBy(pageable,currentDate);
            }
        }
        long postCount = postsRepository.getPostCountByStandardConditions(currentDate);
        List<PostsDTO> postsDTOS = list.stream().map(this::convertToDto).collect(toList());

        return new PostsForResponse(postsDTOS,postCount);
    }

    public PostForGetByIdPost getPostById(Integer id) {
        Posts post = postsRepository.findPostsById(id);
        return Objects.isNull(post) ? null : modelMapperForByIdPost.map(post, PostForGetByIdPost.class);
    }

    public PostsForResponse getPostBySearch(Integer offset, Integer limit, String search) {
        Long currentDate = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli();

        if (search.isEmpty()) {
            return getPosts(offset, limit, "recent");
        }
        PageRequest pageable = PageRequest.of(offset / limit, limit);

        long postCount = postsRepository.getCountFindByQuery(currentDate,search);

        List<Posts> list = new ArrayList<>(postsRepository.findByQuery(pageable, currentDate, search));
        List<PostsDTO> postsDTOS = list.stream().map(this::convertToDto).collect(toList());

        return new PostsForResponse(postsDTOS,postCount);
    }

    public PostsForResponse getPostByDate(Integer offset, Integer limit, String date) {
        Long currentDate = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli();

        LocalDate dateForFind = LocalDate.parse(date);

        Long dateForFind1 = dateForFind.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
        Long dateForFind2 = dateForFind.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();

        Pageable pageable = PageRequest.of(offset / limit, limit);
        List<Posts> list = new ArrayList<>(postsRepository.findByDate(pageable, currentDate, dateForFind1, dateForFind2));

        long postCount = postsRepository.getCountFindByDate(currentDate,dateForFind1,dateForFind2);

        List<PostsDTO> postsDTOS = list.stream().map(this::convertToDto).collect(toList());

        return new PostsForResponse(postsDTOS,postCount);
    }

    public PostsForResponse getPostsByTag(Integer offset, Integer limit, String tagFromFront) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        Tags tag = tagsRepository.findTagByName(tagFromFront);

        if (tag == null) {
            return new PostsForResponse(new ArrayList<>(),0);
        }
        List<Tag2post> tag2posts = tag.getTag2posts();

        List<Integer> postIds = new ArrayList<>();
        tag2posts.forEach(tag2post -> postIds.add(tag2post.getPostId()));

        Long currentDate = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli();

        long postCount = postsRepository.getCountFindByIds(currentDate,postIds);

        List<Posts> list = postsRepository.findByIds(pageable, currentDate, postIds);
        List<PostsDTO> listDTO = list.stream().map(this::convertToDto).collect(toList());

        return new PostsForResponse(listDTO,postCount);
    }

    private PostsDTO convertToDto(Posts post) {
        return Objects.isNull(post) ? null : modelMapperToPostsDTO.map(post, PostsDTO.class);
    }

    public void addViewToPostIfNotModeratorAndWriter(Integer postId,String sessionId){
        Posts post = postsRepository.findPostsById(postId);

        if (post!=null){
            if (!authService.isModeratorBySessionId(sessionId) && !authService.isWriterBySessionId(postId,sessionId)){
                post.setViewCount(post.getViewCount()+1);
                postsRepository.save(post);
            }
        }
    }

    public JSONObject like(Integer postId){
        return likeOrDislikeJsonCreateAndAddToDB(postId,1);
    }

    public JSONObject dislike(Integer postId){
        return likeOrDislikeJsonCreateAndAddToDB(postId,-1);
    }

    private JSONObject likeOrDislikeJsonCreateAndAddToDB(Integer postId,Integer likeOrDislike){
        JSONObject json = new JSONObject();
        String sessionID = RequestContextHolder.currentRequestAttributes().getSessionId();
        Integer userId = authService.findUserIdBySession(sessionID);

        if (userId == null){
            json.put("result",false);
            return json;
        }

        PostVotes postVotesFromBd = postVotersRepository.getPostVotesByUserIdAndPostId(postId,userId);

        if (postVotesFromBd == null){
            PostVotes postVotes = new PostVotes();
            postVotes.setPostId(postId);
            postVotes.setUserId(userId);
            postVotes.setValue(likeOrDislike);
            postVotes.setTime(LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli());
            postVotersRepository.save(postVotes);
            json.put("result",true);
            return json;
        }else if (postVotesFromBd.getValue() == (likeOrDislike*-1)){
            postVotesFromBd.setValue(likeOrDislike);
            postVotersRepository.save(postVotesFromBd);
            json.put("result",true);
            return json;
        }

        json.put("result",false);
        return json;
    }

}
