package lesson11.cglib;

/**
 * Created on 2018\8\29 0029 by yancongcong
 */
public class TestProxy {

    public static void main(String[] args) {
        Hello hello = CglibDynamicProxy.getInstance().getProxy(Hello.class);
        hello.sayHello();
    }

}
