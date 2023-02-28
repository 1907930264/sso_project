package com.yxj.ssoserver.controller;

import com.yxj.ssoserver.common.RedisUtils;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("test")
public class TestController {

    @Resource
    private RedisUtils redisUtils;

    @GetMapping("a")
    public Object test(Authentication authentication){
        Object principal = authentication.getPrincipal();
        redisUtils.set("authencation",principal);
        Object authencation = redisUtils.get("authencation");
        try {
            throw new Exception("手动测试报错");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return authencation;
    }
}
