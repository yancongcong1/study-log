package com.ycc.framework.ioc.bean;

import com.ycc.framework.mvc.annotation.Param;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Get the way to the class by reflect.
 *
 * created by ycc at 2018\4\24 0024
 */
public final class ReflectionUtil {

    private static Logger logger = Logger.getLogger(ReflectionUtil.class);

    public static Object getInstance(Class<?> clazz) {
        Object object;
        try {
            object = clazz.newInstance();
        } catch (InstantiationException e) {
            logger.error("类初始化异常", e);
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            logger.error("非法参数异常", e);
            throw new RuntimeException(e);
        }
        return object;
    }

    public static Object invokeMethod(Object object, Method method, Map<String, Object> paramMap) {
        Object result;
        try {
            method.setAccessible(true);
            List<Object> params = new ArrayList<>();
            Parameter[] parameters = method.getParameters();
            for (Parameter parameter : parameters) {
                Param param = parameter.getAnnotation(Param.class);
                Object value = paramMap.get(param.value());
                if (value == null) {
                    throw new RuntimeException("参数传递出错, 缺少参数" + param.value());
                }
                params.add(value);
            }
            result = method.invoke(object, params.toArray());
        } catch (Exception e) {
            logger.error("调用方法出错", e);
            throw new RuntimeException(e);
        }
        return result;
    }

    public static void setField(Object object, Field field, Object value) {
        try {
            field.setAccessible(true);
            field.set(object, value);
        } catch (IllegalAccessException e) {
            logger.error("设置属性值出错", e);
            throw new RuntimeException(e);
        }
    }
}
