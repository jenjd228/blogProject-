package com.blog.spring.controller;

import net.minidev.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiGeneralController {

    @GetMapping("api/init/")
    public JSONObject init() {
        JSONObject json = new JSONObject();
        json.put("title","DevPub");
        json.put("subtitle","Рассказы разработчиков");
        json.put("phone","+7 903 666-44-55");
        json.put("email","mail@mail.ru");
        json.put("copyright","Дмитрий Сергеев");
        json.put("copyrightFrom","2005");
        return json;
    }


}
