package org.kettle.scheduler.system.biz.shiro.session;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionKey;

import javax.servlet.ServletRequest;
import java.io.Serializable;

/**
 * 自定义SessionManager改善读取session的方式
 * 因为每次获取session都是从redis缓存数据库中获取
 * 为了提高性能做了优化，如果request中存在就从中获取
 * 不存在再从redis缓存数据库中获取
 *
 * @author admin
 */
public class MySessionManager extends DefaultWebSessionManager {

    @Override
    protected Session retrieveSession(SessionKey sessionKey) throws UnknownSessionException {
        Serializable sessionId = getSessionId(sessionKey);
        ServletRequest request = null;
        if (sessionKey instanceof WebSessionKey) {
            request = ((WebSessionKey) sessionKey).getServletRequest();
            if (request != null && sessionId != null) {
                Session session = (Session) request.getAttribute(sessionId.toString());
                if (session != null) {
                    return session;
                }
            }
        }

        Session session = super.retrieveSession(sessionKey);
        if (request != null && sessionId != null) {
            request.setAttribute(sessionId.toString(), session);
        }
        return session;
    }
}
