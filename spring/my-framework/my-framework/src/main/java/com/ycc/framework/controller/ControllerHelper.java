package com.ycc.framework.controller;

import com.ycc.framework.annotation.Mapping;
import com.ycc.framework.loader.ClassHelper;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Pattern the request and the handler, put them into a map to store.
 * Return the handler of the request.
 *
 * created by ycc at 2018\4\24 0024
 */
public class ControllerHelper {

    private static final Map<Request, Handler> ACTION_MAP = new HashMap<Request, Handler>();

    static {
        Set<Class<?>> controllerClassSet = ClassHelper.getControllerClassSet();
        for (Class<?> clazz : controllerClassSet) {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(Mapping.class)) {
                    Mapping annotation = method.getAnnotation(Mapping.class);
                    String requestMethod = annotation.method();
                    String url = annotation.url();
                    Request request = new Request(requestMethod, url);
                    Handler handler = new Handler(clazz, method);
                    ACTION_MAP.put(request, handler);
                }
            }
        }
    }

    public static Handler getHandler(String requestMethod, String url) {
        Request request = new Request(requestMethod, url);
        return ACTION_MAP.get(request);
    }

    public static Map<Request, Handler> getAllHandlers() {
        return ACTION_MAP;
    }
}
