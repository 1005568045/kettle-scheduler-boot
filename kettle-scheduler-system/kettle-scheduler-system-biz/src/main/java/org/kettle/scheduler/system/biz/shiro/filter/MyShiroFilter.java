package org.kettle.scheduler.system.biz.shiro.filter;

import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * url过滤配置
 *
 * @author admin
 */
@Configuration
public class MyShiroFilter {

    /**
     * 对shiro的过滤进行设置
     *
     * @return {@link ShiroFilterChainDefinition}
     */
    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        // 可以匿名访问静态资源
        chainDefinition.addPathDefinition("favicon.ico", "anon");
        chainDefinition.addPathDefinition("/css/**", "anon");
        chainDefinition.addPathDefinition("/fonts/**", "anon");
		chainDefinition.addPathDefinition("/img/**", "anon");
		chainDefinition.addPathDefinition("/js/**", "anon");
		chainDefinition.addPathDefinition("/lib/**", "anon");
		// 设置登录功能可以访问
		chainDefinition.addPathDefinition("/sys/login/in.do", "anon");
		// 设置swagger可以访问
		chainDefinition.addPathDefinition("/swagger-ui.html", "anon");
		chainDefinition.addPathDefinition("/webjars/**", "anon");
		chainDefinition.addPathDefinition("/v2/**", "anon");
		chainDefinition.addPathDefinition("/swagger-resources/**", "anon");
		// 设置需要管理员才能访问
		chainDefinition.addPathDefinition("/sys/user/getUserByUsername.do", "authc");
		chainDefinition.addPathDefinition("/sys/user/**", "authc,roles[admin]");

        /* roles[python]中定义需要当前角色才能具有访问权限, 自定义Filter中的Object o就是roles["角色1","角色2"]中的数据
        chainDefinition.addPathDefinition("/my/python", "authc,roles[python]");*/

        // 需要登录才能访问
        chainDefinition.addPathDefinition("/**", "authc");
        return chainDefinition;
    }
}
