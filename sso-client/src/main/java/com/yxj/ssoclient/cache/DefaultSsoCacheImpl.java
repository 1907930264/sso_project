package com.yxj.ssoclient.cache;

import com.yxj.ssoclient.context.SsoClientContext;
import com.yxj.ssoclient.context.SsoClientContextHolder;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultSsoCacheImpl implements SsoCache<SsoClientContext> {

    //全局唯一sso认证信息缓存
    private static Map<String, SsoClientContext> cacheMap = new ConcurrentHashMap<>();

    @Override
    public SsoClientContext get(String key) {
        return cacheMap.get(key);
    }

    @Override
    public void set(SsoClientContext ssoClientContext) {
        cacheMap.put(ssoClientContext.getToken(), ssoClientContext);
    }

    @Override
    public Boolean remove(String key) {
        SsoClientContext context = cacheMap.remove(key);
        return context != null;
    }

    @Override
    public Boolean reSetAuthenticationByCache(String key) {
        SsoClientContext context = get(key);
        if (context != null) {
            SsoClientContextHolder.setContext(context);
            return true;
        }
        return false;
    }
}
