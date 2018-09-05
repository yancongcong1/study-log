package lesson11.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created on 2018\8\29 0029 by yancongcong
 * <p>
 * jdk动态代理需要代理类实现InvocationHandler接口
 * jdk动态代理只能代理实现了接口的类，是针对于接口方法的一种代理
 * jdk动态代理在程序运行时通过反射生成代理类class文件
 */
public class JdkDynamicProxy implements InvocationHandler {

    private Object target;

    public JdkDynamicProxy(Object target) {
        this.target = target;
    }

    public <T> T getProxy() {
        return (T) Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("jdk 动态代理： before------------");
        final Object result = method.invoke(target, args);
        System.out.println("jdk 动态代理： after------------");
        return result;
    }
}
