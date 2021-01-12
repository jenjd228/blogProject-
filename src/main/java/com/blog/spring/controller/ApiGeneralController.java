package com.blog.spring.controller;

import com.blog.spring.DTO.*;
import com.blog.spring.model.GlobalSettings;
import com.blog.spring.model.PostsForResponse;
import com.blog.spring.repository.GlobalSettingsRepository;
import com.blog.spring.service.AuthService;
import com.blog.spring.service.GeneralService;
import com.blog.spring.service.PostsService;
import net.minidev.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.List;

@RestController
@RequestMapping(value = "/api/")
public class ApiGeneralController {

    private final Logger logger = Logger.getLogger(ApiGeneralController.class);

    private final GeneralService generalService;

    private final GlobalSettingsRepository globalSettingsRepository;

    private final PostsService postsService;

    private final AuthService authService;

    ApiGeneralController(AuthService authService, GeneralService generalService, GlobalSettingsRepository globalSettingsRepository, PostsService postsService) {
        this.authService = authService;
        this.generalService = generalService;
        this.globalSettingsRepository = globalSettingsRepository;
        this.postsService = postsService;
    }

    @GetMapping("init")
    public JSONObject init() {

        logger.info("/api/init - Запрос данных инициализации");

        JSONObject json = new JSONObject();
        json.put("title", "DevPub");
        json.put("subtitle", "Рассказы разработчиков");
        json.put("phone", "+7 903 666-44-55");
        json.put("email", "mail@mail.ru");
        json.put("copyright", "Дмитрий Сергеев");
        json.put("copyrightFrom", "2005");
        return json;
    }

    @GetMapping("tag")
    public ResponseEntity<PostsForResponse> getTags(@RequestParam(required = false) String query) {
        logger.info("/api/tag - Запрос на список всех тегов");

        JSONObject jo = new JSONObject();
        List<TagForTagsDTO> tags = generalService.findTagsByQuery(query);

        jo.put("tags", tags);

        return new ResponseEntity(jo, HttpStatus.OK);
    }

    @GetMapping("calendar")
    public ResponseEntity<PostsForResponse> getCalendar(@RequestParam(required = false) String year) {
        logger.info("/api/calendar - Запрос на календарь " + year);

        CalendarDTO calendarDTO = generalService.getCalendar(year);
        return new ResponseEntity(calendarDTO, HttpStatus.OK);
    }

    @GetMapping("settings")
    public JSONObject getGlobalSettings() {
        logger.info("/api/settings - Запрос на настройки блога");

        return generalService.getSettings();
    }

    @PutMapping("settings")
    public void putGlobalSettings(@RequestBody SettingsDTO settingsDTO) {
        logger.info("/api/settings - Запрос на изменение настроек блога");
        generalService.putSettings(settingsDTO);
    }

    @GetMapping("statistics/all")
    public ResponseEntity<StatisticDTO> getAllStatistics() {
        logger.info("/api/statistics/all - Запрос на статистику блога");

        if (generalService.isStatisticPublic()) {
            return new ResponseEntity(generalService.getAllStatistics(), HttpStatus.OK);
        } else if (authService.isModeratorBySessionId(RequestContextHolder.currentRequestAttributes().getSessionId())) {// нужна проверка на модератора
            return new ResponseEntity(generalService.getAllStatistics(), HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("statistics/my")
    public StatisticDTO getMyStatistic(){
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        logger.info("/api/statistics/my - Запрос на статистику блога пользователя с сессией : "+sessionId);
        return generalService.getMyStatistics(sessionId);
    }

    @PostMapping("moderation")
    public JSONObject moderation(@RequestBody ModerationDTO moderationDTO){
        logger.info("/moderation - Модерация поста с id : "+moderationDTO.getPost_id());
        return postsService.moderation(moderationDTO);
    }

    @PostMapping("comment")
    public ResponseEntity<JSONObject> comment(@RequestBody AddCommentDTO addCommentDTO){
        logger.info("/comment - Добавление комментария к посту "+addCommentDTO.toString());
        return generalService.comment(addCommentDTO);
    }

    @PutMapping("post/{id}")
    public void updatePost(@PathVariable Integer id,@RequestBody AddPostDTO addPostDTO){
        logger.info("/post/{id} - Обновление поста с id "+id+"  "+addPostDTO.toString());
        generalService.updatePost(id,addPostDTO);
    }
}
