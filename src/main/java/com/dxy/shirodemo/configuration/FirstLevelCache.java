package com.dxy.shirodemo.configuration;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.MapCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Set;

/**
 * @ClassName FirstLevelCache
 * @Author duxiaoyu
 * @Date 2020/12/10 11:11
 * @Version 1.0
 */
public class FirstLevelCache<K, V> implements Cache<K, V> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FirstLevelCache.class);

    private final String PERMISSION_CACHE_PREFIX = "shiro_permission:";

    @Override
    public V get(K k) throws CacheException {
        return null;
    }

    @Override
    public V put(K k, V v) throws CacheException {
        return null;
    }

    @Override
    public V remove(K k) throws CacheException {
        return null;
    }

    @Override
    public void clear() throws CacheException {

    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Set<K> keys() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
    }
}
