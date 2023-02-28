package com.yxj.ssoclient.config;

import com.yxj.ssoclient.filters.SsoAuthenticationFilter;
import com.yxj.ssoclient.filters.SsoPreAuthenticationFilter;
import com.yxj.ssoclient.spi.adapt.CustomAuthenticationAdapt;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;

@Configuration
public class SsoFilterConfig {

    @Resource
    private SsoMetaProperty ssoMetaProperty;

    @Resource
    private CustomAuthenticationAdapt customAuthenticationAdapt;

    @Bean
    public FilterRegistrationBean registrationAuthenticationFilter(){
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(new SsoAuthenticationFilter(ssoMetaProperty,customAuthenticationAdapt));
        registrationBean.setOrder(0);
        if (!CollectionUtils.isEmpty(ssoMetaProperty.getUrlPattens())){
            registrationBean.setUrlPatterns(ssoMetaProperty.getUrlPattens());
        }
        return registrationBean;
    }


    @Bean
    public FilterRegistrationBean registrationPreAuthenticationFilter(){
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(new SsoPreAuthenticationFilter(ssoMetaProperty));
        registrationBean.setOrder(-1);
        if (!CollectionUtils.isEmpty(ssoMetaProperty.getUrlPattens())){
            registrationBean.setUrlPatterns(ssoMetaProperty.getUrlPattens());
        }
        return registrationBean;
    }
}
