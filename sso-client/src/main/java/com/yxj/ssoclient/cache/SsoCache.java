package com.yxj.ssoclient.cache;

public interface SsoCache<T> {

    /**
     * 获取数据
     * @param key
     * @return
     */
    T get(String key);

    /**
     * 存入缓存
     * @param t
     */
    void set(T t);

    /**
     * 删除缓存key
     * @param key
     * @return
     */
    Boolean remove(String key);


    /**
     * 重新设置认证上下文
     * @param key
     */
    Boolean reSetAuthenticationByCache(String key);

}
