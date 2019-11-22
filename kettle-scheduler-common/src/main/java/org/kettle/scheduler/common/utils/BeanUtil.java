package org.kettle.scheduler.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.cglib.beans.BeanGenerator;
import org.springframework.cglib.beans.BeanMap;

import java.beans.PropertyDescriptor;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * bean操作工具
 * <pre>
 *     继承spring的Bean工具,并实现自己的一些逻辑处理
 * </pre>
 *
 * @author lyf
 */
@Slf4j
public class BeanUtil extends BeanUtils {

    /**
     * 动态为bean添加新的属性和值
     * @param oldBean 原来的数据对象
     * @param newPropertyMap 新的属性和值
     * @return 新的对象实例
     */
    public static Object dynamicBean(Object oldBean, Map<String, Object> newPropertyMap) {
        if (CollectionUtil.isEmpty(newPropertyMap)) {
            return oldBean;
        } else {
            // bean生成工具
            BeanGenerator generator = new BeanGenerator();
            // 设置继承类
            generator.setSuperclass(oldBean.getClass());
            // 添加新的属性
            Map<String, Class> propertyMap = new HashMap<>(newPropertyMap.size());
            newPropertyMap.forEach((key, value) -> propertyMap.put(key, value.getClass()));
            BeanGenerator.addProperties(generator, propertyMap);
            // 创建新的bean
            BeanMap beanMap = BeanMap.create(generator.create());
            // 为新的bean赋值
            newPropertyMap.forEach(beanMap::put);
            // 返回后需要把旧的值拷贝过来
            Object newBean = beanMap.getBean();
            copyProperties(oldBean, newBean);
            return newBean;
        }
    }

    /**
     * 把对象数据复制到目标对象
     * @param source 源数据
     * @param target 目标对象实例
     */
    public static void copyProperties(@NotNull Object source, @NotNull Object target) {
        copyProperties(source, target, getNullPropertyNames(source));
    }

    /**
     * 根据指定类复制对象并返回结果
     * @param source 源数据
     * @param clazz 目标对象class类
     * @param <T> 目标对象类型
     * @return 返回目标对象实例
     */
    public static <T> T copyProperties(@NotNull Object source, @NotNull Class<T> clazz) {
        T target;
        try {
            target = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            String message = MessageFormat.format("{0} 对象无法实例化", clazz);
            log.error(message, e);
            throw new RuntimeException(message);
        }
        copyProperties(source, target, getNullPropertyNames(source));
        return target;
    }

    /**
     * 获取bean对象中值为空的属性名称
     * @param source 源数据对象
     * @return 值为空的属性名称数组
     */
    public static String[] getNullPropertyNames(@NotNull Object source) {
        BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (StringUtil.isEmpty(srcValue)) {
                emptyNames.add(pd.getName());
            }
        }
        int size = emptyNames.size();
        return emptyNames.toArray(new String[size]);
    }
}
