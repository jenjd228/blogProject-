package com.blog.spring.service;

import com.blog.spring.DTO.AddPostDTO;
import com.blog.spring.DTO.ModerationDTO;
import com.blog.spring.DTO.PostsDTO;
import com.blog.spring.model.*;
import com.blog.spring.repository.*;
import net.minidev.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

import java.net.InetAddress;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
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

    private final GlobalSettingsRepository globalSettingsRepository;

    private final Tag2PostRepository tag2PostRepository;

    private final AuthService authService;

    private final PostVotersRepository postVotersRepository;

    @Value("${server.port}")
    private String port;

    public PostsService(GlobalSettingsRepository globalSettingsRepository, PostVotersRepository postVotersRepository, AuthService authService, ModelMapper modelMapperToPostsDTO, ModelMapper modelMapperForByIdPost, PostsRepository postsRepository, TagsRepository tagsRepository, Tag2PostRepository tag2PostRepository) {
        this.globalSettingsRepository = globalSettingsRepository;
        this.postVotersRepository = postVotersRepository;
        this.authService = authService;
        this.modelMapperForByIdPost = modelMapperForByIdPost;
        this.modelMapperToPostsDTO = modelMapperToPostsDTO;
        this.postsRepository = postsRepository;
        this.tagsRepository = tagsRepository;
        this.tag2PostRepository = tag2PostRepository;
    }

    public PostsForResponse getPosts(Integer offset, Integer limit, String mode) {
        Long currentDate = LocalDateTime.now().toInstant(ZoneOffset.UTC).getEpochSecond();
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
                list = postsRepository.findBy(pageable, currentDate);
            }
            default -> {
                pageable = PageRequest.of(offset / limit, limit, Sort.by("time").descending());
                list = postsRepository.findBy(pageable, currentDate);
            }
        }
        long postCount = postsRepository.getPostCountByStandardConditions(currentDate);
        List<PostsDTO> postsDTOS = list.stream().map(this::convertToDto).collect(toList());

        return new PostsForResponse(postsDTOS, postCount);
    }

    public PostForGetByIdPost getPostById(Integer id) {
        Posts post = postsRepository.findPostsById(id);
        post.getUser().setPhoto("http://" + InetAddress.getLoopbackAddress().getHostName() + ":" + port + "/" + post.getUser().getPhoto());
        return Objects.isNull(post) ? null : modelMapperForByIdPost.map(post, PostForGetByIdPost.class);
    }

    public PostsForResponse getPostBySearch(Integer offset, Integer limit, String search) {
        Long currentDate = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli();

        if (search.isEmpty()) {
            return getPosts(offset, limit, "recent");
        }
        PageRequest pageable = PageRequest.of(offset / limit, limit);

        long postCount = postsRepository.getCountFindByQuery(currentDate, search);

        List<Posts> list = new ArrayList<>(postsRepository.findByQuery(pageable, currentDate, search));
        List<PostsDTO> postsDTOS = list.stream().map(this::convertToDto).collect(toList());

        return new PostsForResponse(postsDTOS, postCount);
    }

    public PostsForResponse getPostByDate(Integer offset, Integer limit, String date) {
        Long currentDate = LocalDateTime.now().toInstant(ZoneOffset.UTC).getEpochSecond();

        LocalDate dateForFind = LocalDate.parse(date);

        Long dateForFind1 = dateForFind.atStartOfDay(ZoneOffset.UTC).toInstant().getEpochSecond();
        Long dateForFind2 = dateForFind.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant().getEpochSecond();

        Pageable pageable = PageRequest.of(offset / limit, limit);
        List<Posts> list = new ArrayList<>(postsRepository.findByDate(pageable, currentDate, dateForFind1, dateForFind2));

        long postCount = postsRepository.getCountFindByDate(currentDate, dateForFind1, dateForFind2);

        List<PostsDTO> postsDTOS = list.stream().map(this::convertToDto).collect(toList());

        return new PostsForResponse(postsDTOS, postCount);
    }

    public PostsForResponse getPostsByTag(Integer offset, Integer limit, String tagFromFront) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        Tags tag = tagsRepository.findTagByName(tagFromFront);

        if (tag == null) {
            return new PostsForResponse(new ArrayList<>(), 0);
        }
        List<Tag2post> tag2posts = tag.getTag2posts();

        List<Integer> postIds = new ArrayList<>();
        tag2posts.forEach(tag2post -> postIds.add(tag2post.getKey().getPostId()));

        Long currentDate = LocalDateTime.now().toInstant(ZoneOffset.UTC).getEpochSecond();

        long postCount = postsRepository.getCountFindByIds(currentDate, postIds);

        List<Posts> list = postsRepository.findByIds(pageable, currentDate, postIds);
        List<PostsDTO> listDTO = list.stream().map(this::convertToDto).collect(toList());

        return new PostsForResponse(listDTO, postCount);
    }

    private PostsDTO convertToDto(Posts post) {
        return Objects.isNull(post) ? null : modelMapperToPostsDTO.map(post, PostsDTO.class);
    }

    public void addViewToPostIfNotModeratorAndWriter(Integer postId, String sessionId) {
        Posts post = postsRepository.findPostsById(postId);

        if (post != null) {
            if (!authService.isModeratorBySessionId(sessionId) && !authService.isWriterBySessionId(postId, sessionId)) {
                post.setViewCount(post.getViewCount() + 1);
                postsRepository.save(post);
            }
        }
    }

    public JSONObject like(Integer postId) {
        return likeOrDislikeJsonCreateAndAddToDB(postId, 1);
    }

    public JSONObject dislike(Integer postId) {
        return likeOrDislikeJsonCreateAndAddToDB(postId, -1);
    }

    private JSONObject likeOrDislikeJsonCreateAndAddToDB(Integer postId, Integer likeOrDislike) {
        JSONObject json = new JSONObject();
        String sessionID = RequestContextHolder.currentRequestAttributes().getSessionId();
        Integer userId = authService.findUserIdBySession(sessionID);

        if (userId == null) {
            return null;
        }

        PostVotes postVotesFromBd = postVotersRepository.getPostVotesByUserIdAndPostId(postId, userId);

        if (postVotesFromBd == null) {
            PostVotes postVotes = new PostVotes();
            postVotes.setPostId(postId);
            postVotes.setUserId(userId);
            postVotes.setValue(likeOrDislike);
            postVotes.setTime(LocalDateTime.now().toInstant(ZoneOffset.UTC).getEpochSecond());
            postVotersRepository.save(postVotes);
            json.put("result", true);
            return json;
        } else if (postVotesFromBd.getValue() == (likeOrDislike * -1)) {
            postVotesFromBd.setValue(likeOrDislike);
            postVotersRepository.save(postVotesFromBd);
            json.put("result", true);
            return json;
        }

        json.put("result", false);
        return json;
    }

    public JSONObject addPost(AddPostDTO addPostDTO) {
        String sessionID = RequestContextHolder.currentRequestAttributes().getSessionId();
        Integer userId = authService.findUserIdBySession(sessionID);

        if (userId != null) {
            JSONObject json = new JSONObject();
            HashMap<String, String> errors = new HashMap<>();

            boolean result = true;

            if (addPostDTO.getText().length() < 5) {
                result = false;
                errors.put("text", "Текст публикации слишком короткий");
            }

            if (addPostDTO.getTitle().length() < 3) {
                result = false;
                errors.put("title", "Заголовок не установлен");
            }

            json.put("result", result);

            if (result) {
                Posts newPost = new Posts();

                newPost.setViewCount(0);
                newPost.setIsActive(addPostDTO.getActive());
                if (globalSettingsRepository.isPOST_PREMODERATION().equals("YES")) {
                    newPost.setModerationStatus(ModerationStatus.NEW);
                } else {
                    newPost.setModerationStatus(ModerationStatus.ACCEPTED);
                }
                newPost.setTitle(addPostDTO.getTitle());
                newPost.setText(addPostDTO.getText());
                newPost.setTime(Math.max(addPostDTO.getTimestamp(), LocalDateTime.now().toInstant(ZoneOffset.UTC).getEpochSecond()));
                newPost.setUserId(userId);

                if (addPostDTO.getTags().size() != 0) {
                    addPostDTO.getTags().forEach(tagsRepository::saveIgnoreDuplicateKey);
                    newPost.setTags(tagsRepository.findTagsIdByNameIn(addPostDTO.getTags()));
                }
                postsRepository.save(newPost);

                return json;
            }


            json.put("errors", errors);
            return json;
        }
        return null;
    }

    public JSONObject moderation(ModerationDTO moderationDTO) {
        JSONObject json = new JSONObject();
        String sessionID = RequestContextHolder.currentRequestAttributes().getSessionId();
        Users user = authService.findUserBySession(sessionID);
        Posts post = postsRepository.findPostsById(moderationDTO.getPost_id());

        if (user == null) {
            return null;
        }

        if (user.getIsModerator() == 1 && post != null) {
            json.put("result", true);
            post.setModeratorId(user.getId());
            if (moderationDTO.getDecision().equals("accept")) {
                post.setModerationStatus(ModerationStatus.ACCEPTED);
            } else {
                post.setModerationStatus(ModerationStatus.DECLINED);
            }
            postsRepository.save(post);
            return json;
        }
        json.put("result", false);
        return json;
    }

    public PostsForResponse getPostsForModeration(Integer offset, Integer limit, String status) {
        String sessionID = RequestContextHolder.currentRequestAttributes().getSessionId();
        Integer userId = authService.findUserIdBySession(sessionID);

        if (userId != null) {
            PageRequest pageable = PageRequest.of(offset / limit, limit);
            List<Posts> list;
            long postCount;

            switch (status) {
                case "new" -> {
                    list = postsRepository.findPostsByStatus(pageable, ModerationStatus.NEW, userId);
                    postCount = postsRepository.findPostsCountByStatus(ModerationStatus.NEW, userId);
                }
                case "declined" -> {
                    list = postsRepository.findPostsByStatus(pageable, ModerationStatus.DECLINED, userId);
                    postCount = postsRepository.findPostsCountByStatus(ModerationStatus.DECLINED, userId);

                }
                case "accepted" -> {
                    list = postsRepository.findPostsByStatus(pageable, ModerationStatus.ACCEPTED, userId);
                    postCount = postsRepository.findPostsCountByStatus(ModerationStatus.ACCEPTED, userId);
                }
                default -> {
                    list = new ArrayList<>();
                    postCount = 0;
                }
            }
            List<PostsDTO> postsDTOS = list.stream().map(this::convertToDto).collect(toList());

            return new PostsForResponse(postsDTOS, postCount);
        }
        return null;
    }

    public PostsForResponse my(Integer offset, Integer limit, String status) {
        String sessionID = RequestContextHolder.currentRequestAttributes().getSessionId();
        Integer userId = authService.findUserIdBySession(sessionID);
        if (userId != null) {
            PageRequest pageable = PageRequest.of(offset / limit, limit);
            List<ModerationStatus> moderationStatusList = new ArrayList<>();
            int isActive = 1;

            switch (status) {
                case "pending" -> {
                    moderationStatusList.add(ModerationStatus.NEW);
                }
                case "declined" -> {
                    moderationStatusList.add(ModerationStatus.DECLINED);
                }
                case "published" -> {
                    moderationStatusList.add(ModerationStatus.ACCEPTED);
                }
                default -> {
                    isActive = 0;
                    moderationStatusList.add(ModerationStatus.ACCEPTED);
                    moderationStatusList.add(ModerationStatus.NEW);
                    moderationStatusList.add(ModerationStatus.DECLINED);
                }
            }

            List<Posts> posts = postsRepository.findPostsByUserIdAndStatus(pageable, isActive, userId, moderationStatusList);

            List<PostsDTO> postsDTOS = posts.stream().map(this::convertToDto).collect(toList());
            long postsCount = postsRepository.findCountPostsByUserIdAndStatus(isActive, userId, moderationStatusList);

            return new PostsForResponse(postsDTOS, postsCount);
        }
        return null;
    }
}
