package com.yxj.ssoclient.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "sso-config")
public class SsoMetaProperty {

    //sso服务端访问地址
    private String ssoServerUrl;

    //sso客户端全局过滤开关
    private Boolean filterGlobalSwitch;

    //需要过滤的匹配路径
    private List<String> urlPattens;

    //自定义客户端鉴权开关
    private Boolean customAuthenticationSwitch;

    //客户端回调地址
    private String callbackUrl;

    //解析token地址
    private String analysisTokenUrl;


    public String getSsoServerUrl() {
        return ssoServerUrl;
    }

    public void setSsoServerUrl(String ssoServerUrl) {
        this.ssoServerUrl = ssoServerUrl;
    }

    public Boolean getFilterGlobalSwitch() {
        return filterGlobalSwitch;
    }

    public void setFilterGlobalSwitch(Boolean filterGlobalSwitch) {
        this.filterGlobalSwitch = filterGlobalSwitch;
    }

    public List<String> getUrlPattens() {
        return urlPattens;
    }

    public void setUrlPattens(List<String> urlPattens) {
        this.urlPattens = urlPattens;
    }

    public Boolean getCustomAuthenticationSwitch() {
        return customAuthenticationSwitch;
    }

    public void setCustomAuthenticationSwitch(Boolean customAuthenticationSwitch) {
        this.customAuthenticationSwitch = customAuthenticationSwitch;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getAnalysisTokenUrl() {
        return analysisTokenUrl;
    }

    public void setAnalysisTokenUrl(String analysisTokenUrl) {
        this.analysisTokenUrl = analysisTokenUrl;
    }
}
