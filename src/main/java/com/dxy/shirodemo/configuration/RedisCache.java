package com.dxy.shirodemo.configuration;

import com.dxy.shirodemo.utils.JedisUtil;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.SerializationUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @ClassName RedisCache 使用redis作为shiro的权限缓存
 * @Author duxiaoyu
 * @Date 2020/12/9 14:00
 * @Version 1.0
 */
@Component
public class RedisCache<K, V> implements Cache<K, V> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisCache.class);

    private final static String PERMISSION_CACHE_PREFIX = "shiro_permission:";

    private byte[] getKey(K key) {
        if (key instanceof String) {
            return String.format("%s%s", PERMISSION_CACHE_PREFIX, key).getBytes();
        }
        return SerializationUtils.serialize(key);
    }

    @Autowired
    private JedisUtil jedisUtil;

    @Override
    public V get(K k) throws CacheException {
        LOGGER.info("查询缓存中的权限数据 ...");
        if (Objects.isNull(k)) {
            throw new NullPointerException("redisCache key is null ...");
        }
        byte[] key = getKey(k);
        byte[] value = jedisUtil.get(key);
        if (value != null && value.length > 0) {
            return (V) SerializationUtils.deserialize(value);
        }
        return null;
    }

    @Override
    public V put(K k, V v) throws CacheException {
        LOGGER.info("向缓存中保存权限数据 ...");
        if (Objects.isNull(k) || Objects.isNull(v)) {
            throw new NullPointerException("redisCache key or value is null ...");
        }
        byte[] key = getKey(k);
        byte[] value = SerializationUtils.serialize(v);
        jedisUtil.put(key, value, 600);
        return v;
    }

    @Override
    public V remove(K k) throws CacheException {
        LOGGER.info("删除缓存中的权限数据 ...");
        if (Objects.isNull(k)) {
            throw new NullPointerException("redisCache key is null ...");
        }
        byte[] key = getKey(k);
        jedisUtil.remove(key);
        return get(k);
    }

    @Override
    public void clear() throws CacheException {
        // 清空不做实现，可能会导致redis其他缓存数据丢失
    }

    @Override
    public int size() {
        LOGGER.info("获取缓存中的权限数据个数 ...");
        Set<byte[]> caches = jedisUtil.keys(PERMISSION_CACHE_PREFIX);
        if (CollectionUtils.isEmpty(caches)) {
            return 0;
        }
        return caches.size();
    }

    @Override
    public Set<K> keys() {
        LOGGER.info("获取缓存中的所有权限数据key ...");
        Set<byte[]> caches = jedisUtil.keys(PERMISSION_CACHE_PREFIX);
        if (CollectionUtils.isEmpty(caches)) {
            return Collections.emptySet();
        }
        Set<K> keySet = caches.stream().map(x -> (K) SerializationUtils.deserialize(x)).collect(Collectors.toSet());
        return keySet;
    }

    @Override
    public Collection<V> values() {
        LOGGER.info("获取缓存中的所有权限数据value ...");
        Set<byte[]> caches = jedisUtil.keys(PERMISSION_CACHE_PREFIX);
        if (CollectionUtils.isEmpty(caches)) {
            return Collections.emptySet();
        }
        Set<V> valueSet = caches.stream().map(key -> get((K) SerializationUtils.deserialize(key))).collect(Collectors.toSet());
        return valueSet;
    }
}
