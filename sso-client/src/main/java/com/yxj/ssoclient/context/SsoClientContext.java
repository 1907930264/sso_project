package com.yxj.ssoclient.context;

import java.io.Serializable;
import java.util.Map;

public class SsoClientContext implements Serializable {

    private static final long serialVersionUID = 3728877563912075885L;

    /**
     * sso token
     */
    private String token;

    /**
     * 用户名？
     */
    private String name;

    /**
     * 唯一ID，用户id？
     */
    private String uniqueId;

    /**
     * 是否认证
     */
    private Boolean authenticated = false;

    /**
     * 扩展配置
     */
    private Map<String, Object> bizData;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public Boolean getAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(Boolean authenticated) {
        this.authenticated = authenticated;
    }

    public Map<String, Object> getBizData() {
        return bizData;
    }

    public void setBizData(Map<String, Object> bizData) {
        this.bizData = bizData;
    }
}
