package com.blog.spring.controller;

import com.blog.spring.DTO.*;
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
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity init() {

        logger.info("/api/init - Запрос данных инициализации");

        JSONObject json = new JSONObject();
        json.put("title", "DevPub");
        json.put("subtitle", "Рассказы разработчиков");
        json.put("phone", "+7 903 666-44-55");
        json.put("email", "mail@mail.ru");
        json.put("copyright", "Дмитрий Сергеев");
        json.put("copyrightFrom", "2005");

        return new ResponseEntity(json, HttpStatus.OK);
    }

    @GetMapping("tag")
    public ResponseEntity getTags(@RequestParam(required = false) String query) {
        logger.info("/api/tag - Запрос на список всех тегов");

        JSONObject jo = new JSONObject();
        List<TagForTagsDTO> tags = generalService.findTagsByQuery(query);

        jo.put("tags", tags);

        return new ResponseEntity(jo, HttpStatus.OK);
    }

    @GetMapping("calendar")
    public ResponseEntity getCalendar(@RequestParam(required = false) String year) {
        logger.info("/api/calendar - Запрос на календарь " + year);

        CalendarDTO calendarDTO = generalService.getCalendar(year);
        return new ResponseEntity(calendarDTO, HttpStatus.OK);
    }

    @GetMapping("settings")
    public ResponseEntity getGlobalSettings() {
        logger.info("/api/settings - Запрос на настройки блога");

        return new ResponseEntity(generalService.getSettings(), HttpStatus.OK);
    }

    @PutMapping("settings")
    public void putGlobalSettings(@RequestBody SettingsDTO settingsDTO) {
        logger.info("/api/settings - Запрос на изменение настроек блога");
        generalService.putSettings(settingsDTO);
    }

    @GetMapping("statistics/all")
    public ResponseEntity getAllStatistics() {
        logger.info("/api/statistics/all - Запрос на статистику блога");

        if (generalService.isStatisticPublic()) {
            return new ResponseEntity(generalService.getAllStatistics(), HttpStatus.OK);
        } else if (authService.isModeratorBySessionId(RequestContextHolder.currentRequestAttributes().getSessionId())) {// нужна проверка на модератора
            return new ResponseEntity(generalService.getAllStatistics(), HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("statistics/my")
    public ResponseEntity getMyStatistic() {
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        logger.info("/api/statistics/my - Запрос на статистику блога пользователя с сессией : " + sessionId);
        return new ResponseEntity(generalService.getMyStatistics(sessionId), HttpStatus.OK);
    }

    @PostMapping("moderation")
    public ResponseEntity moderation(@RequestBody ModerationDTO moderationDTO) {
        logger.info("/moderation - Модерация поста с id : " + moderationDTO.getPost_id());
        return new ResponseEntity(postsService.moderation(moderationDTO), HttpStatus.OK);
    }

    @PostMapping("comment")
    public ResponseEntity comment(@RequestBody AddCommentDTO addCommentDTO) {
        logger.info("/comment - Добавление комментария к посту " + addCommentDTO.toString());
        return new ResponseEntity(generalService.comment(addCommentDTO), HttpStatus.OK);
    }

    @PutMapping("post/{id}")
    public void updatePost(@PathVariable Integer id, @RequestBody AddPostDTO addPostDTO) {
        logger.info("/post/{id} - Обновление поста с id " + id + "  " + addPostDTO.toString());
        generalService.updatePost(id, addPostDTO);
    }

    @PostMapping("profile/my")
    public ResponseEntity updateMyProfile(@RequestBody UpdateProfileDTO updateProfileDTO){
        logger.info("profile/my - Обновление данных о пользователе с возможным удалением фото "+updateProfileDTO.toString());
        return new ResponseEntity(generalService.updateProfileWithoutPhoto(updateProfileDTO),HttpStatus.OK);
    }

    @PostMapping(value = "profile/my",consumes = "multipart/form-data")
    public @ResponseBody ResponseEntity updateMyProfileWithPhoto(UpdateProfileWithPhotoDTO updateProfileWithPhotoDTO){
        logger.info("profile/my - Обновление данных о пользователе с фото "+updateProfileWithPhotoDTO.toString());
        return new ResponseEntity(generalService.updateProfileWithPhoto(updateProfileWithPhotoDTO),HttpStatus.OK);
    }

    @PostMapping(value = "image",consumes = "multipart/form-data")
    public @ResponseBody ResponseEntity image(MultipartFile file){
        logger.info("image - Картинка");
        JSONObject response = generalService.image(file);
        if (response.get("imageLocalPath") == null){
            return new ResponseEntity(response,HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(response.get("imageLocalPath"),HttpStatus.BAD_REQUEST);
    }
}
