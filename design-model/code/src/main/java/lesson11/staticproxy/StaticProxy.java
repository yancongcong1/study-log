package lesson11.staticproxy;

/**
 * Created on 2018\8\29 0029 by yancongcong
 *
 * 静态代理需要实现和被代理类同样的接口，class文件在执行之前已经被编译好了
 */
public class StaticProxy implements Hello {

    private HelloImpl customInterface;

    public StaticProxy(HelloImpl customInterface) {
        this.customInterface = customInterface;
    }

    @Override
    public void sayHello() {
        System.out.println("静态代理 before-------");
        customInterface.sayHello();
        System.out.println("静态代理 after-------");
    }
}
