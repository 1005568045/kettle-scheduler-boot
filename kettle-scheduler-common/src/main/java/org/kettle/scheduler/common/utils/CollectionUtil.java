package org.kettle.scheduler.common.utils;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Predicate;

/**
 * 集合处理的工具类
 *
 * @author lyf
 */
public class CollectionUtil {

    /**
     * 判断<code>Map</code>是否为<code>null</code>或空<code>{}</code>
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return (map == null) || (map.size() == 0);
    }

    /**
     * 判断Map是否不为<code>null</code>和空<code>{}</code>
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * 判断<code>Collection</code>是否为<code>null</code>或空数组<code>[]</code>。
     */
    public static boolean isEmpty(Collection<?> collection) {
        return (collection == null) || (collection.size() == 0);
    }

    /**
     * 判断Collection是否不为<code>null</code>和空数组<code>[]</code>。
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * 判断<code>Enumeration</code>是否有元素
     */
    public static boolean hasItems(Enumeration<?> enums) {
        return (enums != null) && (enums.hasMoreElements());
    }

    /**
     * 判断<code>Enumeration</code>是否没有元素
     *
     * @param enums ## @see Enumeration
     * @return 如果没有元素, 则返回<code>true</code>
     */
    public static boolean hasNotItems(Enumeration<?> enums) {
        return !hasItems(enums);
    }

    /**
     * 判断<code>Iterator</code>是否有元素
     */
    public static boolean hasItems(Iterator<?> iters) {
        return (iters != null) && (iters.hasNext());
    }

    /**
     * 判断<code>Iterator</code>是否没有元素
     */
    public static boolean hasNotItems(Iterator<?> iters) {
        return !hasItems(iters);
    }

    /**
     * 查找列表中的元素，返回第一个匹配项
     */
    public static <T> T find(Collection<? extends T> collection, Predicate<T> predicate) {
        if (collection != null && predicate != null) {
            for (T item : collection) {
                if (predicate.test(item)) {
                    return item;
                }
            }
        }
        return null;
    }
}
