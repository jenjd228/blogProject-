package com.blog.spring.controller;

import com.blog.spring.model.GlobalSettings;
import com.blog.spring.repository.GlobalSettingsRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Controller
public class DefaultController {

    private final Logger logger = Logger.getLogger(DefaultController.class);

    @Autowired
    private GlobalSettingsRepository globalSettingsRepository;

    @PostConstruct
    public void init(){

        logger.info("Инициализация настроек блога");

        if (globalSettingsRepository.count() == 0){
            GlobalSettings multiUserMode = new GlobalSettings(1,"MULTIUSER_MODE","Многопользовательский режим","YES");
            GlobalSettings postPreModeration = new GlobalSettings(2,"POST_PREMODERATION","Премодерация постов","YES");
            GlobalSettings statisticsIsPublic = new GlobalSettings(3,"STATISTICS_IS_PUBLIC","Показывать всем статистику блога","YES");
            List<GlobalSettings> settings = Arrays.asList(multiUserMode, postPreModeration, statisticsIsPublic);
            globalSettingsRepository.saveAll(settings);
        }
    }

    @RequestMapping(value = { "/", "/index" }, method = RequestMethod.GET)
    public String index()
    {
        logger.info("/ Возвращена главная страница");
        return "index";
    }

    @RequestMapping(method = {RequestMethod.OPTIONS, RequestMethod.GET}, value = "/**/{path:[^\\.]*}")
    public String redirectToIndex() {
        return "forward:/";
    }

}
