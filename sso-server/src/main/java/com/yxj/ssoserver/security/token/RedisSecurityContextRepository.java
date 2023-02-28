package com.yxj.ssoserver.security.token;

import com.yxj.ssoserver.common.RedisUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class RedisSecurityContextRepository implements SecurityContextRepository {

    protected final Log logger = LogFactory.getLog(this.getClass());

    /**
     * SecurityContext instance used to check for equality with default (unauthenticated)
     * content
     */
    private final Object contextObject = SecurityContextHolder.createEmptyContext();


    @Resource
    private RedisUtils redisUtils;

    @Resource
    private TokenProvider tokenProvider;


    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        HttpServletRequest request = requestResponseHolder.getRequest();
        String ssoToken = null;
        if (StringUtils.hasText(ssoToken = tokenProvider.getSsoToken(request))){
            return loadContextByRedis(ssoToken);
        }
        return SecurityContextHolder.createEmptyContext();
    }

    /**
     * 从redis中取出认证信息
     * @param key
     * @return
     */
    private SecurityContext loadContextByRedis(String key){
        SecurityContext context = (SecurityContext)redisUtils.get(key);
        if (context == null){
            context = SecurityContextHolder.createEmptyContext();
        }
        return context;
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        //响应头中取出生产的token为key存入redis
        String ssoToken = response.getHeader("ssoToken");
        if (StringUtils.hasText(ssoToken)){
            redisUtils.set(ssoToken, context,24*60*60);
        }
    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        //从请求参数获取token去判断
        String ssoToken = request.getParameter("ssoToken");
        if (StringUtils.hasText(ssoToken)){
            return containsContextByRedis(ssoToken);
        }

        //从cookie中获取token去判断
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for (Cookie cookie : cookies) {
                if ("ssoToken".equals(cookie.getName())) {
                    return containsContextByRedis(cookie.getValue());
                }
            }

        }
        return false;
    }

    /**
     * 判断redis中是否有token
     * @param key
     * @return
     */
    private boolean containsContextByRedis(String key){
       return redisUtils.hasKey(key);
    }
}
