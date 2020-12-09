package com.dxy.shirodemo.configuration;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionKey;

import javax.servlet.ServletRequest;
import java.io.Serializable;
import java.util.Objects;

/**
 * @ClassName CustomSessionManager
 * @Author duxiaoyu
 * @Date 2020/12/9 10:04
 * @Version 1.0
 */
public class CustomSessionManager extends DefaultWebSessionManager {

    /**
     * 优化多次访问redis获取session的问题
     *
     * @param sessionKey
     * @return
     * @throws UnknownSessionException
     */
    @Override
    protected Session retrieveSession(SessionKey sessionKey) throws UnknownSessionException {
        Serializable sessionId = getSessionId(sessionKey);
        ServletRequest request = null;
        if (sessionKey instanceof WebSessionKey) {
            request = ((WebSessionKey) sessionKey).getServletRequest();
        }
        // 每次尝试先从request中获取，如果没有再从redis中获取
        if (Objects.nonNull(request) && Objects.nonNull(sessionId)) {
            Session session = (Session) request.getAttribute(sessionId.toString());
            if (Objects.nonNull(session)) {
                return session;
            }
        }

        Session session = super.retrieveSession(sessionKey);
        if (Objects.nonNull(request) && Objects.nonNull(sessionId)) {
            request.setAttribute(sessionId.toString(), session);
        }
        return session;
    }
}
