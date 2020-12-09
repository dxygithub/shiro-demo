package com.dxy.shirodemo.configuration;

import org.apache.shiro.cache.AbstractCacheManager;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @ClassName RedisCacheManager 自定义RedisCacheManager
 * @Author duxiaoyu
 * @Date 2020/12/9 14:35
 * @Version 1.0
 */
public class RedisCacheManager implements CacheManager {

    @Autowired
    private RedisCache redisCache;

    /**
     * 使用自定义的redisCache
     *
     * @param s
     * @param <K>
     * @param <V>
     * @return
     * @throws CacheException
     */
    @Override
    public <K, V> Cache<K, V> getCache(String s) throws CacheException {
        return redisCache;
    }
}
