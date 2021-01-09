package com.blog.spring.service;

import com.blog.spring.DTO.*;
import com.blog.spring.model.GlobalSettings;
import com.blog.spring.model.Users;
import com.blog.spring.repository.GlobalSettingsRepository;
import com.blog.spring.repository.PostVotersRepository;
import com.blog.spring.repository.PostsRepository;
import com.blog.spring.repository.Tag2PostRepository;
import net.minidev.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

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

    public GeneralService(PostVotersRepository postVotersRepository,ModelMapper modelMapperToStatisticDTO,AuthService authService,GlobalSettingsRepository globalSettingsRepository,ModelMapper modelMapperToTagForTagsDTO, PostsRepository postsRepository, Tag2PostRepository tag2PostRepository) {
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
            //tags = ((ArrayList<Tags>) tagsRepository.findAll()).stream().map(this::convertToTagForTagsDTO).collect(toList());
        } else {
            lists = tag2PostRepository.findTagsByQueryAndSortByCountOfPosts(query);
            //tags = tagsRepository.findTagsByNameContaining(query).stream().map(this::convertToTagForTagsDTO).collect(toList());
        }
        
        tags = lists.stream().map(this::convertToDto).collect(toList());

        if (tags.size() != 0) {
            for (TagForTagsDTO tag : tags) {
                tag.setNormalWeight(postCount);
            }
            //tags.sort(tagWeightComparator);
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

    public CalendarDTO getCalendar(String year){
        List<String> years = postsRepository.getCalendar();
        years.remove(null);

        long dateForFind1;
        long dateForFind2;

        if (year == null || year.isEmpty()){

            java.util.Calendar calendar = java.util.Calendar.getInstance(java.util.TimeZone.getDefault(), java.util.Locale.getDefault());
            calendar.setTime(new java.util.Date());

            String currentYear = String.valueOf(calendar.get(java.util.Calendar.YEAR));

            LocalDate dateForFind = LocalDate.parse(currentYear+"-01-01");

            dateForFind1 = dateForFind.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
            dateForFind2 = dateForFind.plusYears(1).atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();

        }else {
            LocalDate dateForFind = LocalDate.parse(year+"-01-01");

            dateForFind1 = dateForFind.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
            dateForFind2 = dateForFind.plusYears(1).atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();

        }

        List<List<String>> posts = postsRepository.getCalendarByYear(dateForFind1,dateForFind2);

        HashMap<String,String> postsForDTO = new HashMap<>();

        posts.forEach(para -> postsForDTO.put(para.get(0),para.get(1)));

        return new CalendarDTO(years,postsForDTO);
    }

    public JSONObject getSettings(){
        List<GlobalSettings> settingsList = globalSettingsRepository.findAll();
        JSONObject json = new JSONObject();

        json.put(settingsList.get(0).getCode(), settingsList.get(0).getValue().equals("YES"));
        json.put(settingsList.get(1).getCode(), settingsList.get(1).getValue().equals("YES"));
        json.put(settingsList.get(2).getCode(), settingsList.get(2).getValue().equals("YES"));
        return json;
    }

    public StatisticDTO getAllStatistics(){
        Statistics statistics = postVotersRepository.getStatistics();
        return modelMapperToStatisticDTO.map(statistics,StatisticDTO.class);
    }

    public StatisticDTO getMyStatistics(String sessionId){
        Integer id = authService.findUserIdBySession(sessionId);
        Iterable<Integer> list = postsRepository.getPostIdsByUserId(id);
        Statistics statistics = postVotersRepository.getMyStatistics(list);

        return modelMapperToStatisticDTO.map(statistics,StatisticDTO.class);
    }

    public void putSettings(){
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        Users user = authService.findUserBySession(sessionId);

        if (user!= null && user.getIsModerator() == 1){
            /**
             *insert into blog.global_settings(code,value) VALUES('MULTIUSER_MODE','NO') ON DUPLICATE KEY UPDATE value = VALUES(value);
             * TODO
             *
             */
        }
    }

    public boolean isStatisticPublic(){
        return globalSettingsRepository.isStatisticsPublic().equals("YES");
    }

}
