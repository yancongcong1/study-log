package lesson11.jdk;

/**
 * Created on 2018\8\29 0029 by yancongcong
 */
public class TestProxy {

    public static void main(String[] args) {
        Hello hello = new JdkDynamicProxy(new HelloImpl()).getProxy();
        hello.sayHello();
    }

}
