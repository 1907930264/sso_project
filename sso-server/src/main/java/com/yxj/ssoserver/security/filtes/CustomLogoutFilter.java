package com.yxj.ssoserver.security.filtes;

import com.yxj.ssoserver.controller.GlobalExceptionController;
import com.yxj.ssoserver.security.token.TokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

//@Component
public class CustomLogoutFilter extends GenericFilterBean {

    private static final Logger log = LoggerFactory.getLogger(CustomLogoutFilter.class);


    @Resource
    private TokenProvider tokenProvider;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        String ssoToken = tokenProvider.getSsoToken(httpServletRequest);
        if (ssoToken == null){
            log.error("注销失败，请求中无有效token，{}",httpServletRequest,new IllegalArgumentException());
        }
    }
}
