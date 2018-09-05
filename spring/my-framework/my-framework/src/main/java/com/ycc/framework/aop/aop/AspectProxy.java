package com.ycc.framework.aop.aop;

import com.ycc.framework.aop.proxy.Proxy;
import com.ycc.framework.aop.proxy.ProxyChain;

import java.lang.reflect.Method;

/**
 * Created on 2018\9\4 0004 by yancongcong
 */
public class AspectProxy implements Proxy {
    @Override
    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result;

        Class<?> targetClass = proxyChain.getTargetClass();
        Method targetMethod = proxyChain.getTargetMethod();
        Object[] methodParams = proxyChain.getMethodParams();

        begin();
        try {
            if (intercept(targetClass, targetMethod, methodParams)) {
                before(targetClass, targetMethod, methodParams);
                result = proxyChain.doProxyChain();
                after(targetClass, targetMethod, methodParams, result);
            } else {
                result = proxyChain.doProxyChain();
            }
        } catch (Exception e) {
            throw e;
        } finally {
            end();
        }
        return result;
    }

    public void begin() {}

    public void end() {}

    public boolean intercept(Class<?> targetClass, Method targetMethod, Object[] methodParams) throws Throwable {
        return true;
    }

    public void before(Class<?> targetClass, Method targetMethod, Object[] methodParams) throws Throwable {

    }

    public void after(Class<?> targetClass, Method targetMethod, Object[] methodParams, Object result) throws Throwable {

    }
}
