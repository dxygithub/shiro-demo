package com.dxy.shirodemo.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Objects;
import java.util.Set;

/**
 * @ClassName JedisUtil
 * @Author duxiaoyu
 * @Date 2020/12/8 14:23
 * @Version 1.0
 */
@Component
public class JedisUtil {

    @Autowired
    private JedisPool jedisPool;

    private Jedis getJedis() {
        Jedis jedis = jedisPool.getResource();
        if (Objects.isNull(jedis)) {
            throw new NullPointerException("get Jedis Fail...");
        }
        return jedis;
    }

    /**
     * 添加: 可配置过期时间，单位：秒
     *
     * @param key
     * @param value
     * @return
     */
    public byte[] put(byte[] key, byte[] value, int expireTime) {
        Jedis jedis = getJedis();
        try {
            jedis.setex(key, expireTime, value);
        } finally {
            jedis.close();
        }
        return key;
    }

    /**
     * 添加
     *
     * @param key
     * @param value
     * @return
     */
    public byte[] put(byte[] key, byte[] value) {
        Jedis jedis = getJedis();
        try {
            jedis.set(key, value);
        } finally {
            jedis.close();
        }
        return key;
    }

    /**
     * 获取
     *
     * @param key
     * @return
     */
    public byte[] get(byte[] key) {
        Jedis jedis = getJedis();
        try {
            byte[] value = jedis.get(key);
            return value;
        } finally {
            jedis.close();
        }
    }

    /**
     * 移除
     *
     * @param key
     */
    public void remove(byte[] key) {
        Jedis jedis = getJedis();
        try {
            jedis.del(key);
        } finally {
            jedis.close();
        }
    }

    /**
     * 获取全部session
     *
     * @param prefix
     * @return
     */
    public Set<byte[]> keys(String prefix) {
        Jedis jedis = getJedis();
        try {
            return jedis.keys(String.format("%s*", prefix).getBytes());
        } finally {
            jedis.close();
        }
    }


}
