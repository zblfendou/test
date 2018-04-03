package com.aop.controller;

import com.aop.annotation.SystemControllerLog;
import com.aop.models.User;
import com.aop.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by zbl on 2017/8/22.
 */
@Controller
@RequestMapping("/aop")
public class IndexController {
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);
    @Inject private UserService userService;

    @RequestMapping("/add.do")
    @SystemControllerLog(description = "添加测试controller层")
    public String add(@RequestParam("name")String name, @RequestParam("pwd")String pwd, HttpServletRequest request) {
        logger.debug("name:{}", name);
        logger.debug("pwd:{}",pwd);
        User user = new User();
        user.setName(name);
        user.setPwd(pwd);
         userService.add(user);

        return "success";
    }
}
