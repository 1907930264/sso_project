package com.yxj.ssoserver.security.handler;

import com.yxj.ssoserver.common.RedisUtils;
import com.yxj.ssoserver.security.filtes.CustomLogoutFilter;
import com.yxj.ssoserver.security.token.TokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class CustomLogoutHandler implements LogoutHandler {

    private static final Logger log = LoggerFactory.getLogger(CustomLogoutHandler.class);

    @Resource
    private TokenProvider tokenProvider;

    @Resource
    private RedisUtils redisUtils;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String ssoToken = tokenProvider.getSsoToken(request);
        if (ssoToken == null){
            log.error("注销登陆失败#请求中无有效token，{}",request,new IllegalArgumentException());
        }
        redisUtils.del(ssoToken);
        log.info("注销登陆#删除redis成功");
    }
}
