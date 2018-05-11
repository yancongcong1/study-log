package com.ycc.framework.ioc.bean;

import com.ycc.framework.ioc.loader.ClassHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Manage the relationship of class and it's instance.
 *
 * created by ycc at 2018\4\24 0024
 */
public final class BeanHelper {
    private static final Map<Class<?>, Object> BEAN_SET = new HashMap();

    static {
        Set<Class<?>> classes = ClassHelper.getBeanClassSet();
        for (Class<?> clazz : classes) {
            Object object = ReflectionUtil.getInstance(clazz);
            BEAN_SET.put(clazz, object);
        }
    }

    public static Map<Class<?>, Object> getBeanSet() {
        return BEAN_SET;
    }

    public static <T> T getBean(Class<T> clazz) {
        if (!BEAN_SET.containsKey(clazz)) {
            throw new RuntimeException("bean set not contain this clazz" + clazz.getName());
        }
        return (T) BEAN_SET.get(clazz);
    }
}
