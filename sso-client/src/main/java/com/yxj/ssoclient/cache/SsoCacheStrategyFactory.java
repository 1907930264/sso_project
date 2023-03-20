package com.yxj.ssoclient.cache;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SsoCacheStrategyFactory implements ApplicationContextAware {

    private ApplicationContext applicationContext;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 获取缓存策略
     * @return
     */
    public SsoCache getCacheStrategy(){
        SsoCache ssoCache = applicationContext.getBean(SsoCache.class);
        if (ssoCache != null){
            return ssoCache;
        }

        return new DefaultSsoCacheImpl();
    }
}
