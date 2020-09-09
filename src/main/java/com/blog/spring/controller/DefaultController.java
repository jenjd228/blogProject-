package com.blog.spring.controller;

import com.blog.spring.model.GlobalSettings;
import com.blog.spring.repository.GlobalSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.PostConstruct;

@Controller
public class DefaultController {

    @Autowired
    GlobalSettingsRepository globalSettingsRepository;

    @PostConstruct
    public void init(){
        if (globalSettingsRepository.count() == 0){
            GlobalSettings multiUserMode = new GlobalSettings(1,"MULTIUSER_MODE","Многопользовательский режим","YES");
            GlobalSettings postPreModeration = new GlobalSettings(2,"POST_PREMODERATION","Премодерация постов","YES");
            GlobalSettings statisticsIsPublic = new GlobalSettings(3,"STATISTICS_IS_PUBLIC","Показывать всем статистику блога","YES");
            globalSettingsRepository.save(multiUserMode);
            globalSettingsRepository.save(postPreModeration);
            globalSettingsRepository.save(statisticsIsPublic);
        }
    }

    @RequestMapping(value = { "/", "/index" }, method = RequestMethod.GET)
    public String index() {
        return "index";
    }

}
