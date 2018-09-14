package com.ycc.framework.aop.aop;

import com.ycc.framework.aop.annotation.Aspect;
import com.ycc.framework.aop.proxy.Proxy;
import com.ycc.framework.aop.proxy.ProxyManager;
import com.ycc.framework.ioc.bean.BeanHelper;
import com.ycc.framework.ioc.loader.ClassHelper;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * Created on 2018\9\4 0004 by yancongcong
 */
public class AopHelper {

    static {
        try {
            Map<Class<?>, Set<Class<?>>> proxyMap = createProxyMap();
            Map<Class<?>, List<Proxy>> targetMap = createTargetMap(proxyMap);
            for (Map.Entry<Class<?>, List<Proxy>> entry : targetMap.entrySet()) {
                Class<?> targetClass = entry.getKey();
                List<Proxy> proxyList = entry.getValue();
                Object proxy = ProxyManager.createProxy(targetClass, proxyList);
                BeanHelper.setBean(targetClass, proxy);
            }
        } catch (Throwable t) {
            System.out.println("出现错误！");
        }
    }

    /**
     * 获取Aspect注解所有类
     * @param aspect
     * @return
     * @throws Throwable
     */
    private static Set<Class<?>> createTargetClassSet(Aspect aspect) throws Throwable {
        Set<Class<?>> targetClassSet = new HashSet<>();
        Class<? extends Annotation> annotation = aspect.value();
        if (annotation != null && !annotation.equals(Aspect.class)) {
            targetClassSet.addAll(ClassHelper.getClassSetByAnnotation(annotation));
        }
        return targetClassSet;
    }

    /**
     * 获取代理类和目标集合类的映射关系(代理类必须带有@Aspect注解并且继承AspectProxy)
     * @return
     * @throws Throwable
     */
    private static Map<Class<?>, Set<Class<?>>> createProxyMap() throws Throwable {
        Map<Class<?>, Set<Class<?>>> proxyMap = new HashMap<>();
        Set<Class<?>> proxyClassSet = ClassHelper.getClassSetBySuper(AspectProxy.class);
        for (Class<?> clazz : proxyClassSet) {
            if (clazz.isAnnotationPresent(Aspect.class)) {
                Aspect aspect = clazz.getAnnotation(Aspect.class);
                Set<Class<?>> classSet = createTargetClassSet(aspect);
                proxyMap.put(clazz, classSet);
            }
        }
        return proxyMap;
    }

    /**
     * 获取目标类与代理对象列表之间的映射关系
     * @return
     * @throws Throwable
     */
    private static Map<Class<?>, List<Proxy>> createTargetMap(Map<Class<?>, Set<Class<?>>> proxyMap) throws Throwable {
        Map<Class<?>, List<Proxy>> targetMap = new HashMap<>();
        for (Map.Entry<Class<?>, Set<Class<?>>> proxyEntry : proxyMap.entrySet()) {
            Class<?> proxyClass = proxyEntry.getKey();
            Set<Class<?>> targetClasses = proxyEntry.getValue();
            for (Class<?> clazz : targetClasses) {
                Proxy proxy = (Proxy) proxyClass.newInstance();
                if (targetMap.containsKey(clazz)) {
                    targetMap.get(clazz).add(proxy);
                } else {
                    List<Proxy> proxyList = new ArrayList<>();
                    proxyList.add(proxy);
                    targetMap.put(clazz, proxyList);
                }
            }
        }
        return targetMap;
    }

}
