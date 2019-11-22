package org.kettle.scheduler.core.init;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 运行kettle初始化
 *
 * @author lyf
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({KettleInit.class})
public @interface EnableEtlKettle {
}
