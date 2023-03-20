package com.yxj.ssoserver.security.filtes;

import com.alibaba.fastjson.JSON;
import com.yxj.ssoserver.common.RedisUtils;
import com.yxj.ssoserver.common.RestResponse;
import com.yxj.ssoserver.security.token.TokenProvider;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
@Order(-1)
public class LoginVerifyFilter extends GenericFilterBean {

    private static final String MATCHER_URL = "/sso/loginPage.html";

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private TokenProvider tokenProvider;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        long start = System.currentTimeMillis();
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        String callbackUrl = httpServletRequest.getParameter("callbackUrl");

        RequestMatcher matcher = new AntPathRequestMatcher(MATCHER_URL);
        String ssoToken = null;
        if (matcher.matcher(httpServletRequest).isMatch() && (ssoToken = tokenProvider.getSsoToken(httpServletRequest)) != null) {

            if (redisUtils.hasKey(ssoToken)) {
                //如果请求中有token，且还存于redis中，且回调地址不为空，则重定向
                if (StringUtils.hasText(callbackUrl)) {
                    sendRedirect(callbackUrl, ssoToken, httpServletResponse);
                    return;
                }

                //已经认证过了，拦截返回
                RestResponse<String> success = RestResponse.success(ssoToken);
                httpServletResponse.setCharacterEncoding("utf-8");
                httpServletResponse.setContentType("application/json;charset=utf-8");
                PrintWriter writer = httpServletResponse.getWriter();
                Object ok = JSON.toJSONString(success);
                writer.println(ok);
                writer.flush();
                return;
            }


        }

        filterChain.doFilter(servletRequest, servletResponse);
        long end = System.currentTimeMillis();

        System.out.println("use time："+(end - start));

    }

    private void sendRedirect(String callbackUrl, String token, HttpServletResponse httpServletResponse) throws IOException {
        if (callbackUrl == null || callbackUrl.length() == 0) {
            return;
        }
        StringBuffer sb = new StringBuffer(callbackUrl);
        if (callbackUrl.contains("?")) {
            sb.append("&");
        } else {
            sb.append("?");
        }
        sb.append("ssoToken=");
        sb.append(token);
        httpServletResponse.sendRedirect(sb.toString());
    }

}
