package com.redis.controller;

import com.redis.models.RedisModel;
import com.redis.service.RedisModelService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
public class RedisController {
    @Inject
    private RedisModelService service;

    @RequestMapping("/save.do")
    public void save(@RequestParam("redisKey") String redisKey,
                     @RequestParam("name") String name,
                     @RequestParam("tel") String tel,
                     @RequestParam("address") String address) {
        service.save(new RedisModel(redisKey, name, tel, address));
    }

    @RequestMapping("/get.do")
    @ResponseBody
    public RedisModel get(@RequestParam("redisKey") String redisKey) {
        return service.get(redisKey);
    }
}
