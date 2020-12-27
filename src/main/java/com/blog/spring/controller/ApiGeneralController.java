package com.blog.spring.controller;

import com.blog.spring.DTO.CalendarDTO;
import com.blog.spring.DTO.TagForTagsDTO;
import com.blog.spring.model.PostsForResponse;
import com.blog.spring.service.GeneralService;
import com.blog.spring.service.PostsService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ApiGeneralController {

    @Autowired
    private GeneralService generalService;

    @GetMapping("api/init/")
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

    @GetMapping("/api/tag/")
    public ResponseEntity<PostsForResponse> getTags(@RequestParam(required = false) String query){
        JSONObject jo = new JSONObject();
        List<TagForTagsDTO> tags = generalService.findTagsByQuery(query);
        jo.put("tags",tags);
        return new ResponseEntity(jo, HttpStatus.OK);
    }

    @GetMapping("/api/calendar/")
    public ResponseEntity<PostsForResponse> getCalendar(@RequestParam(required = false) String year){
        CalendarDTO calendarDTO = generalService.getCalendar(year);
        return new ResponseEntity(calendarDTO,HttpStatus.OK);
    }
}
