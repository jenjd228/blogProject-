package com.blog.spring.controller;

import com.blog.spring.DTO.AllStatisticsDTO;
import com.blog.spring.DTO.CalendarDTO;
import com.blog.spring.DTO.TagForTagsDTO;
import com.blog.spring.model.GlobalSettings;
import com.blog.spring.model.PostsForResponse;
import com.blog.spring.repository.GlobalSettingsRepository;
import com.blog.spring.service.GeneralService;
import com.blog.spring.service.PostsService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/")
public class ApiGeneralController {

    private final GeneralService generalService;

    private final GlobalSettingsRepository globalSettingsRepository;

    private final PostsService postsService;

    ApiGeneralController(GeneralService generalService, GlobalSettingsRepository globalSettingsRepository,PostsService postsService){
        this.generalService = generalService;
        this.globalSettingsRepository = globalSettingsRepository;
        this.postsService = postsService;
    }

    @GetMapping("init/")
    public JSONObject init() {
        JSONObject json = new JSONObject();
        json.put("title", "DevPub");
        json.put("subtitle", "Рассказы разработчиков");
        json.put("phone", "+7 903 666-44-55");
        json.put("email", "mail@mail.ru");
        json.put("copyright", "Дмитрий Сергеев");
        json.put("copyrightFrom", "2005");
        return json;
    }

    @GetMapping("tag/")
    public ResponseEntity<PostsForResponse> getTags(@RequestParam(required = false) String query){
        JSONObject jo = new JSONObject();
        List<TagForTagsDTO> tags = generalService.findTagsByQuery(query);
        jo.put("tags",tags);
        return new ResponseEntity(jo, HttpStatus.OK);
    }

    @GetMapping("calendar/")
    public ResponseEntity<PostsForResponse> getCalendar(@RequestParam(required = false) String year){
        CalendarDTO calendarDTO = generalService.getCalendar(year);
        return new ResponseEntity(calendarDTO,HttpStatus.OK);
    }

    @GetMapping("settings/")
    public JSONObject getGlobalSettings(){
        List<GlobalSettings> settingsList = globalSettingsRepository.findAll();
        JSONObject json = new JSONObject();
        json.put(settingsList.get(0).getCode(), settingsList.get(0).getValue().equals("YES"));
        json.put(settingsList.get(1).getCode(), settingsList.get(1).getValue().equals("YES"));
        json.put(settingsList.get(2).getCode(), settingsList.get(2).getValue().equals("YES"));
        return json;
    }

    @GetMapping("statistics/all")
    public ResponseEntity<AllStatisticsDTO> getAllStatistics(){
        if (globalSettingsRepository.statisticsIsPublic().equals("YES")){
            return new ResponseEntity(postsService.getAllStatistics(),HttpStatus.OK);
        }else {// нужна проверка на модератора

        }
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }
}
