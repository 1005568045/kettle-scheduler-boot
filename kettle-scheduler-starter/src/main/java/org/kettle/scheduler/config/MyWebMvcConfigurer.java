package org.kettle.scheduler.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 根据自身需求重新配置WebMvc的各项参数
 * 例如：跨域、默认首页、静态资源、消息转换器 等等
 *
 * @author lyf
 */
@Configuration
public class MyWebMvcConfigurer implements WebMvcConfigurer {

    /**
     * 跨域
     * @param registry 域注册器
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "HEAD", "PUT", "DELETE")
                .maxAge(3600)
                .allowedHeaders("Accept", "Origin", "X-Requested-With", "Content-Type", "X-Auth-Token");
    }

    /**
     * 默认首页
     * @param registry 视图注册器
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("login");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }
}
