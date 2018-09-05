package lesson11.cglib;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Created on 2018\8\29 0029 by yancongcong
 *
 * cglib动态代理需要代理类实现MethodInterceptor接口
 * cglib动态代理对指定的目标类生成一个子类，并覆盖其中方法实现增强，但因为采用的是继承，所以不能对final修饰的类进行代理。
 * jdk动态代理在程序运行时通过反射生成代理类class文件
 */
public class CglibDynamicProxy implements MethodInterceptor {

    private static CglibDynamicProxy instance = new CglibDynamicProxy();

    private CglibDynamicProxy() {}

    public static CglibDynamicProxy getInstance() {
        return instance;
    }

    public <T> T getProxy(Class<T> clazz) {
        return (T) Enhancer.create(clazz, this);
    }

    @Override
    public Object intercept(Object target, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        System.out.println("cglib 动态代理： before------------");
        Object result = methodProxy.invokeSuper(target, args);
        System.out.println("cglib 动态代理： after------------");
        return result;
    }
}
