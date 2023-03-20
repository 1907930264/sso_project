package com.yxj.ssoclient.filters;

import com.yxj.ssoclient.cache.SsoCache;
import com.yxj.ssoclient.config.SsoMetaProperty;
import com.yxj.ssoclient.context.SsoClientContext;
import com.yxj.ssoclient.context.SsoClientContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class SsoPreAuthenticationFilter implements Filter {

    private final static String SSO_TOKEN = "ssoToken";

    private SsoMetaProperty ssoMetaProperty;

    private SsoCache ssoCache;

    public SsoPreAuthenticationFilter(SsoMetaProperty ssoMetaProperty, SsoCache ssoCache){
        this.ssoMetaProperty = ssoMetaProperty;
        this.ssoCache = ssoCache;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        try {
            String ssoToken = getSsoToken(httpServletRequest);
            if (ssoToken != null && ssoCache.reSetAuthenticationByCache(ssoToken) && !SsoClientContextHolder.getContext().getAuthenticated()){
                //token不为空且当前未进行查询到认证信息，向ssoServer获取认证信息
                StringBuffer sb = new StringBuffer(ssoMetaProperty.getAnalysisTokenUrl());
                sb.append("?ssoToken="+ssoToken);
                RestTemplate restTemplate = new RestTemplate();
                Map<String,Object> forObject = restTemplate.getForObject(sb.toString(), Map.class);
                Boolean success = (Boolean) forObject.get("success");
                if (success){
                    Map<String, Object> map = (Map<String, Object>) forObject.get("data");
                    if (map != null){
                        SsoClientContext context = new SsoClientContext();
                        context.setAuthenticated(true);
                        context.setToken(ssoToken);
                        context.setUniqueId( map.get("uniqueId").toString());
                        context.setName((String) map.get("name"));
                        if (map.containsKey("bizData")){
                            context.setBizData((Map<String, Object>)map.get("bizData"));
                        }
                        SsoClientContextHolder.setContext(context);
                        ssoCache.set(context);

                        //设置cookie
                        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
                        Cookie cookie = new Cookie(SSO_TOKEN, ssoToken);
                        cookie.setHttpOnly(true);
                        cookie.setDomain(httpServletRequest.getServerName());
                        cookie.setMaxAge(24 * 60 * 60);
                        httpServletResponse.addCookie(cookie);
                    }
                }
            }

            filterChain.doFilter(servletRequest,servletResponse);
        }finally {
            SsoClientContextHolder.clearContext();
        }
    }

    public String getSsoToken(HttpServletRequest request) {
        //从请求头中获取
        String requestHeader = request.getHeader(SSO_TOKEN);
        if (StringUtils.hasText(requestHeader)) {
            return requestHeader;
        }
        //从请求参数获取token
        String ssoToken = request.getParameter(SSO_TOKEN);
        if (StringUtils.hasText(ssoToken)){
            return ssoToken;
        }
        //从cookie中获取token
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for (Cookie cookie : cookies) {
                if (SSO_TOKEN.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
