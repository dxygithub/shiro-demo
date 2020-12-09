package com.dxy.shirodemo.configuration;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @ClassName RedisProperty
 * @Author duxiaoyu
 * @Date 2020/12/8 14:29
 * @Version 1.0
 */
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
@Setter
public class RedisConfiguration {

    private String host;

    private int port;

    private String password;

    private int database;

    private int timeout;

    @Bean
    public JedisPool jedisPool(){
        JedisPoolConfig jedisPoolConfig=new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(100);
        jedisPoolConfig.setMinIdle(10);
        jedisPoolConfig.setMaxIdle(50);
        jedisPoolConfig.setMaxWaitMillis(50 * 1000);
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPoolConfig.setTestOnReturn(true);
        return new JedisPool(jedisPoolConfig,host, port,timeout);
    }

}
