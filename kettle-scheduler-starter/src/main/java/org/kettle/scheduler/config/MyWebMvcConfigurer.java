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
        registry.addViewController("/login").setViewName("login");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

    // /**
    //  * 已经在application.yml中统一配置：spring.jackson.default-property-inclusion: non_empty
    //  * 扩展消息转换器
    //  * 通过index来控制优先级，0优先级最高
    //  * @param converters 消息转换器集
    //  */
    // @Override
    // public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
    //     MappingJackson2HttpMessageConverter om = new MappingJackson2HttpMessageConverter();
    //     om.setObjectMapper(this.nullValueSerializer());
    //     converters.add(0,om);
    // }
    //
    // /**
    //  * 序列化的时候把NULL变成空字符串
    //  */
    // private ObjectMapper nullValueSerializer(){
    //     ObjectMapper MAPPER = new ObjectMapper();
    //     MAPPER.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
    //         @Override
    //         public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
    //             jsonGenerator.writeString("");
    //         }
    //     });
    //     return MAPPER;
    // }
}
