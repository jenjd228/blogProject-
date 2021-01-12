package com.blog.spring.service;

import com.blog.spring.DTO.*;
import com.blog.spring.model.*;
import com.blog.spring.repository.*;
import net.minidev.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Service
public class GeneralService {

    @Qualifier("modelMapperToTagForTagsDTO")
    private final ModelMapper modelMapperToTagForTagsDTO;

    private final ModelMapper modelMapperToStatisticDTO;

    private final Tag2PostRepository tag2PostRepository;

    private final PostsRepository postsRepository;

    private final GlobalSettingsRepository globalSettingsRepository;

    private final AuthService authService;

    private final PostVotersRepository postVotersRepository;

    private final PostCommentsRepository postCommentsRepository;

    private final TagsRepository tagsRepository;

    public GeneralService(TagsRepository tagsRepository,PostCommentsRepository postCommentsRepository, PostVotersRepository postVotersRepository, ModelMapper modelMapperToStatisticDTO, AuthService authService, GlobalSettingsRepository globalSettingsRepository, ModelMapper modelMapperToTagForTagsDTO, PostsRepository postsRepository, Tag2PostRepository tag2PostRepository) {
        this.tagsRepository = tagsRepository;
        this.postCommentsRepository = postCommentsRepository;
        this.postVotersRepository = postVotersRepository;
        this.modelMapperToStatisticDTO = modelMapperToStatisticDTO;
        this.authService = authService;
        this.globalSettingsRepository = globalSettingsRepository;
        this.modelMapperToTagForTagsDTO = modelMapperToTagForTagsDTO;
        this.tag2PostRepository = tag2PostRepository;
        this.postsRepository = postsRepository;
    }

    public List<TagForTagsDTO> findTagsByQuery(String query) {
        List<TagNameAndWeight> lists;
        List<TagForTagsDTO> tags;

        Long postCount = postsRepository.activePostCount(LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli());
        Double maxWeight;

        if (query == null || query.isEmpty()) {
            lists = tag2PostRepository.findTagsAndSortByCountOfPosts();
        } else {
            lists = tag2PostRepository.findTagsByQueryAndSortByCountOfPosts(query);
        }

        tags = lists.stream().map(this::convertToDto).collect(toList());

        if (tags.size() != 0) {
            for (TagForTagsDTO tag : tags) {
                tag.setNormalWeight(postCount);
            }
            maxWeight = tags.get(0).getWeight();
            double x = (1 / maxWeight);

            for (TagForTagsDTO tag : tags) {
                if (tag.getWeight().equals(maxWeight)) {
                    tag.setWeight(1.);
                } else {
                    tag.setWeight(tag.getWeight() * x);
                    if (tag.getWeight() < 0.3) {
                        tag.setWeight(0.3);
                    }
                }
            }
        }

        return tags;
    }

    private TagForTagsDTO convertToDto(TagNameAndWeight post) {
        return Objects.isNull(post) ? null : modelMapperToTagForTagsDTO.map(post, TagForTagsDTO.class);
    }

    public CalendarDTO getCalendar(String year) {
        List<String> years = postsRepository.getCalendar();
        years.remove(null);

        long dateForFind1;
        long dateForFind2;

        if (year == null || year.isEmpty()) {

            java.util.Calendar calendar = java.util.Calendar.getInstance(java.util.TimeZone.getDefault(), java.util.Locale.getDefault());
            calendar.setTime(new java.util.Date());

            String currentYear = String.valueOf(calendar.get(java.util.Calendar.YEAR));

            LocalDate dateForFind = LocalDate.parse(currentYear + "-01-01");

            dateForFind1 = dateForFind.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
            dateForFind2 = dateForFind.plusYears(1).atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();

        } else {
            LocalDate dateForFind = LocalDate.parse(year + "-01-01");

            dateForFind1 = dateForFind.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
            dateForFind2 = dateForFind.plusYears(1).atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();

        }

        List<List<String>> posts = postsRepository.getCalendarByYear(dateForFind1, dateForFind2);

        HashMap<String, String> postsForDTO = new HashMap<>();

        posts.forEach(para -> postsForDTO.put(para.get(0), para.get(1)));

        return new CalendarDTO(years, postsForDTO);
    }

    public JSONObject getSettings() {
        List<GlobalSettings> settingsList = globalSettingsRepository.findAll();
        JSONObject json = new JSONObject();

        json.put(settingsList.get(0).getCode(), settingsList.get(0).getValue().equals("YES"));
        json.put(settingsList.get(1).getCode(), settingsList.get(1).getValue().equals("YES"));
        json.put(settingsList.get(2).getCode(), settingsList.get(2).getValue().equals("YES"));
        return json;
    }

    public StatisticDTO getAllStatistics() {
        Statistics statistics = postVotersRepository.getStatistics();
        return modelMapperToStatisticDTO.map(statistics, StatisticDTO.class);
    }

    public StatisticDTO getMyStatistics(String sessionId) {
        Integer id = authService.findUserIdBySession(sessionId);
        Iterable<Integer> list = postsRepository.getPostIdsByUserId(id);
        Statistics statistics = postVotersRepository.getMyStatistics(list);

        return modelMapperToStatisticDTO.map(statistics, StatisticDTO.class);
    }

    public void putSettings(SettingsDTO settingsDTO) {
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        Users user = authService.findUserBySession(sessionId);

        if (user != null && user.getIsModerator() == 1) {
            String multiuserMode = settingsDTO.isMultiUserMode() ? "YES" : "NO";
            String statisticsIsPublic = settingsDTO.isStatisticIsPublic() ? "YES" : "NO";
            String postPremoderation = settingsDTO.isPostPreModeration() ? "YES" : "NO";
            globalSettingsRepository.updateSettings(multiuserMode, statisticsIsPublic, postPremoderation);
        }
    }

    public boolean isStatisticPublic() {
        return globalSettingsRepository.isStatisticsPublic().equals("YES");
    }

    public ResponseEntity<JSONObject> comment(AddCommentDTO addCommentDTO) {
        JSONObject json = new JSONObject();
        HashMap<String, String> errors = new HashMap<>();

        if (addCommentDTO.getText().isEmpty() || addCommentDTO.getText().length() < 2){
            json.put("result",false);
            errors.put("text","Текст комментария не задан или слишком короткий");
            json.put("errors",errors);
            return new ResponseEntity<>(json, HttpStatus.OK);
        }

        Posts post = postsRepository.findPostsById(addCommentDTO.getPost_id());
        PostComments postComments = new PostComments();

        if (post != null){
            String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
            Users user = authService.findUserBySession(sessionId);

            postComments.setParentId(null);
            if (addCommentDTO.getParent_id() != null){
                if (postCommentsRepository.findPostCommentsById(addCommentDTO.getParent_id()) != null){
                    postComments.setParentId(addCommentDTO.getParent_id());
                }else {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            }
            postComments.setPostId(addCommentDTO.getPost_id());
            postComments.setTime(LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli());
            postComments.setText(addCommentDTO.getText());
            postComments.setUserId(user.getId());

            PostComments postComments1 = postCommentsRepository.save(postComments);

            json.put("id",postComments1.getId());
            return new ResponseEntity<>(json,HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    public JSONObject updatePost(Integer id, AddPostDTO addPostDTO) {
        JSONObject json = new JSONObject();
        HashMap<String, String> errors = new HashMap<>();

        List<Tags> allTagsNames = new ArrayList<>();

        //String sessionID = RequestContextHolder.currentRequestAttributes().getSessionId();
        boolean result = true;

        Posts post = postsRepository.findPostsById(id);

        if (addPostDTO.getText().length() < 5) {
            result = false;
            errors.put("text", "Текст публикации слишком короткий");
        }

        if (addPostDTO.getTitle().length() < 3) {
            result = false;
            errors.put("title", "Заголовок слишком короткий");
        }

        json.put("result", result);

        if (result) {

            post.setIsActive(addPostDTO.getActive());
            post.setModerationStatus(ModerationStatus.NEW);
            post.setTitle(addPostDTO.getTitle());
            post.setText(addPostDTO.getText());
            post.setTime(Math.max(addPostDTO.getTimestamp(), LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()));

            List<Tags> oldTags = post.getTags();
            List<String> newTags  = addPostDTO.getTags();

            for (String string : newTags){
                for (Tags tag : oldTags){
                    //if (!tag.getName().equals(string)){
                        Tags tag1 = new Tags();
                        tag1.setName(string.trim().toUpperCase(Locale.ROOT));
                        allTagsNames.add(tag1);
                   // }
                }
            }
            post.setTags(allTagsNames);
            postsRepository.save(post);
            //List<String> oldOnlyNameTags = new ArrayList<>();

           /* if (addPostDTO.getTags().size() != 0) {

                for (String tag : newTags) {
                    String trimTag = tag.trim().toUpperCase(Locale.ROOT);
                    Tags tags = tagsRepository.findTagByName(trimTag);
                    if (tags == null) {
                        Tags tag1 = new Tags();
                        tag1.setName(trimTag);

                        newTagsList.add(tag1);
                    }
                    allTagsNames.add(trimTag);
                }

                if (newTagsList.size() != 0){
                    post.setTags(newTagsList);
                    postsRepository.save(post);
                }
            }*/

            return json;
        }

        json.put("errors", errors);
        return json;
    }
}
