package org.kettle.scheduler.common.utils;

import org.springframework.util.ObjectUtils;

/**
 * Object操作工具
 * <pre>
 *     继承spring的ObjectUtils工具,并实现自己的一些逻辑处理
 * </pre>
 *
 * @author lyf
 */
public class ObjectUtil extends ObjectUtils {

    /**
     * 判断Object是否是8种基础数据类型及其引用类型
     */
    public static boolean instanceofPrimitive(Object o) {
        boolean bl = false;
        if (o instanceof Number) {
            bl = true;
        } else if (o instanceof Character) {
            bl = true;
        } else if (o instanceof Boolean) {
            bl = true;
        } else if (o instanceof String) {
            bl = true;
        }
        return bl;
    }
}
