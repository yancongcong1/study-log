package com.ycc.framework.ioc;

import com.ycc.framework.annotation.Inject;
import com.ycc.framework.bean.BeanHelper;
import com.ycc.framework.bean.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

/**
 * Set the field to class by container auto.(IOC)
 *
 * created by ycc at 2018\4\24 0024
 */
public class IocHelper {

    static {
        Map<Class<?>, Object> beanSet = BeanHelper.getBeanSet();
        Set<Map.Entry<Class<?>, Object>> entries = beanSet.entrySet();
        for (Map.Entry<Class<?>, Object> entry : entries) {
            Class<?> clazz = entry.getKey();
            Object instance = entry.getValue();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Inject.class)) {
                    Class<?> typeClass = field.getType();
                    Object filedInstance = BeanHelper.getBean(typeClass);
                    if (filedInstance != null) {
                        ReflectionUtil.setField(instance, field, filedInstance);
                    }
                }
            }
        }
    }
}
