package org.kettle.scheduler.system.biz.shiro.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.kettle.scheduler.system.biz.shiro.realm.MyShiroRealm;
import org.kettle.scheduler.system.biz.shiro.session.MyRedisSessionDAO;
import org.kettle.scheduler.system.biz.shiro.session.MySessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * Shiro的安全管理器初始化
 *
 * @author lyf
 */
@Slf4j
@Configuration
public class MyShiroConfig {

    /**
     * 为了保证实现了Shiro内部lifecycle函数的bean执行,也是shiro的生命周期
     * 需要手动注册生命周期的后置管理器
     *
     * @return {@link LifecycleBeanPostProcessor}
     */
    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 在引入spring aop的情况下。
     * 在@Controller注解的类的方法中加入@RequiresRole等shiro注解，会导致该方法无法映射请求，
     * 导致返回404。加入这项配置能解决这个bug，主要是解决 JDK 动态代理问题，需要指定成 CGLIB 代理方式
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    /**
     * 手动注册自定义realm对象到spring中, 否则globalSecurityManager绑定Realm会失败
     *
     * @return {@link org.kettle.scheduler.system.biz.shiro.realm.MyShiroRealm}
     */
    @Bean
    public MyShiroRealm myShiroRealm() {
        return new MyShiroRealm();
    }

    /**
     * 自定义session的增删改查操作
     *
     * @return {@link org.kettle.scheduler.system.biz.shiro.session.MyRedisSessionDAO}
     */
    @Bean
    public MyRedisSessionDAO myRedisSessionDAO() {
        return new MyRedisSessionDAO();
    }

    /**
     * 注入自定义sessionDAO到自定义的session会话管理器中
     *
     * @param myRedisSessionDAO 自定义sessionDAO
     * @return {@link DefaultWebSessionManager}
     */
    @Bean
    public MySessionManager myWebSessionManager(MyRedisSessionDAO myRedisSessionDAO) {
        MySessionManager sessionManager = new MySessionManager();
        sessionManager.setSessionDAO(myRedisSessionDAO);
        return sessionManager;
    }

    /**
     * 全局安全管理器配置
     * 绑定Realm数据对象
     * 绑定自定义session会话管理
     * 绑定自定义缓存实现
     *
     * @return {@link DefaultWebSecurityManager}
     */
    @Bean
    public DefaultWebSecurityManager globalSecurityManager(MyShiroRealm myShiroRealm, MySessionManager sessionManager) {
        DefaultWebSecurityManager webSecurityManager = new DefaultWebSecurityManager();
        webSecurityManager.setRealm(myShiroRealm);
        webSecurityManager.setSessionManager(sessionManager);
        return webSecurityManager;
    }
}
