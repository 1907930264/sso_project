package com.yxj.ssoclient.filters;

import com.yxj.ssoclient.config.SsoMetaProperty;
import com.yxj.ssoclient.context.SsoClientContextHolder;
import com.yxj.ssoclient.spi.adapt.CustomAuthenticationAdapt;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

public class SsoAuthenticationFilter implements Filter {

    private SsoMetaProperty ssoMetaProperty;

    private CustomAuthenticationAdapt customAuthenticationAdapt;

    public SsoAuthenticationFilter(SsoMetaProperty ssoMetaProperty, CustomAuthenticationAdapt customAuthenticationAdapt){
        this.ssoMetaProperty = ssoMetaProperty;
        this.customAuthenticationAdapt = customAuthenticationAdapt;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //全局开关
        if (!ssoMetaProperty.getFilterGlobalSwitch()){
            filterChain.doFilter(servletRequest,servletResponse);
            return;
        }

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        //客户端鉴权
        if (ssoMetaProperty.getCustomAuthenticationSwitch()){
            //自定义客户端鉴权
            if(customAuthenticationAdapt.authentication(httpServletRequest, httpServletResponse)){
                filterChain.doFilter(servletRequest,servletResponse);
                return;
            }
        }else if(SsoClientContextHolder.getContext().getAuthenticated()){
            //默认鉴权
            filterChain.doFilter(servletRequest,servletResponse);
            return;
        }

        //重定向
        redirect(httpServletResponse);

    }


    private void redirect(HttpServletResponse httpServletResponse) throws IOException{
        StringBuffer sb = new StringBuffer(ssoMetaProperty.getSsoServerUrl());
        if(ssoMetaProperty.getSsoServerUrl().contains("?")){
            sb.append("&");
        }else {
            sb.append("?");
        }
        String encode = URLEncoder.encode(ssoMetaProperty.getCallbackUrl(), "UTF-8");
        sb.append("callbackUrl=").append(encode);
        httpServletResponse.sendRedirect(sb.toString());
    }
}
