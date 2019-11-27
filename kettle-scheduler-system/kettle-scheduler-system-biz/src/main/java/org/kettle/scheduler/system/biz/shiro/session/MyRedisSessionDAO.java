package org.kettle.scheduler.system.biz.shiro.session;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.kettle.scheduler.common.utils.RedisUtil;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 重写sessionDAO进行来自定义操作session
 * 注意：放入session中的用户主体对象必须实现Serializable接口进行序列化，否在RedisUtil会序列化失败
 *
 * @author lyf
 */
@Slf4j
public class MyRedisSessionDAO extends AbstractSessionDAO {

    /**
     * 添加sessionId的前缀, 方便session查找
     */
    private final String SESSION_PREFIX = "my-session:";

    @Resource(type = RedisUtil.class)
    private RedisUtil<String, Session> redisUtil;

    @Override
    protected Serializable doCreate(Session session) {
        // 生成sessionId
        Serializable sessionId = generateSessionId(session);
        // 把sessionId和当前session进行绑定
        assignSessionId(session, sessionId);
        // 添加sessionId前缀
        String key = SESSION_PREFIX + sessionId.toString();
        redisUtil.setEx(key, session, 30, TimeUnit.MINUTES);
        log.info("=========>创建session, id=[{}]", key);
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        if (sessionId == null) {
            return null;
        }
        // 添加sessionId前缀
        String key = SESSION_PREFIX + sessionId.toString();
        return redisUtil.get(key);
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        if (session == null || session.getId() == null) {
            return;
        }
        // 添加sessionId前缀
        String key = SESSION_PREFIX + session.getId().toString();
        redisUtil.setEx(key, session, 30, TimeUnit.MINUTES);
    }

    @Override
    public void delete(Session session) {
        if (session == null || session.getId() == null) {
            return;
        }
        // 添加sessionId前缀
        String key = SESSION_PREFIX + session.getId().toString();
        redisUtil.delete(key);
        log.info("=========>删除session,id=[{}]", key);
    }

    @Override
    public Collection<Session> getActiveSessions() {
        Set<String> keys = redisUtil.keys(SESSION_PREFIX + "*");
        log.info("=========>存活的session集,ids=[{}]", keys);
        if (keys == null || keys.isEmpty()) {
            return Collections.emptySet();
        } else {
            return redisUtil.multiGet(keys);
        }
    }
}
