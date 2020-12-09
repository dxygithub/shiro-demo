package com.dxy.shirodemo.configuration;

import com.dxy.shirodemo.utils.JedisUtil;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.SerializationUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @ClassName RedisSessionDao 自定义redisSession
 * @Author duxiaoyu
 * @Date 2020/12/8 14:22
 * @Version 1.0
 */
public class RedisSessionDao extends AbstractSessionDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisSessionDao.class);

    @Autowired
    private JedisUtil jedisUtil;

    private final String SHIRO_SESSION_PREFIX = "shiro-demo:";

    /**
     * 创建session
     *
     * @param session
     * @return
     */
    @Override
    protected Serializable doCreate(Session session) {
        LOGGER.info("开始创建session ...");
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);
        saveSession(session);
        return sessionId;
    }

    /**
     * 读取session
     *
     * @param sessionId
     * @return
     */
    @Override
    protected Session doReadSession(Serializable sessionId) {
        if (Objects.isNull(sessionId)) {
            return null;
        }
        LOGGER.info("开始读取session ...");
        byte[] key = getKey(sessionId.toString());
        byte[] value = jedisUtil.get(key);
        Session session = (Session) SerializationUtils.deserialize(value);
        return session;
    }

    /**
     * 更新session
     *
     * @param session
     * @throws UnknownSessionException
     */
    @Override
    public void update(Session session) throws UnknownSessionException {
        LOGGER.info("开始更新session ...");
        saveSession(session);
    }

    /**
     * 删除session
     *
     * @param session
     */
    @Override
    public void delete(Session session) {
        if (Objects.isNull(session) || Objects.isNull(session.getId())) {
            return;
        }
        LOGGER.info("开始删除session ...");
        byte[] key = getKey(session.getId().toString());
        jedisUtil.remove(key);
    }

    /**
     * 获取所有活跃session
     *
     * @return
     */
    @Override
    public Collection<Session> getActiveSessions() {
        LOGGER.info("开始获取所有活跃session ...");
        Set<byte[]> sessionBytes = jedisUtil.keys(SHIRO_SESSION_PREFIX);
        if (CollectionUtils.isEmpty(sessionBytes)) {
            return Collections.emptySet();
        }
        Set<Session> sessionSet = sessionBytes.stream().map(key -> (Session) SerializationUtils.deserialize(jedisUtil.get(key))).collect(Collectors.toSet());
        return sessionSet;
    }

    private byte[] getKey(String key) {
        return String.format("%s%s", SHIRO_SESSION_PREFIX, key).getBytes();
    }

    private void saveSession(Session session) {
        if (Objects.nonNull(session) && Objects.nonNull(session.getId())) {
            byte[] key = getKey(session.getId().toString());
            byte[] value = SerializationUtils.serialize(session);
            jedisUtil.put(key, value, 600);
        }
    }
}
