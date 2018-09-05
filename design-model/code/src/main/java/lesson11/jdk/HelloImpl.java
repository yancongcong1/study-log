package lesson11.jdk;

/**
 * Created on 2018\8\29 0029 by yancongcong
 */
public class HelloImpl implements Hello {

    @Override
    public void sayHello() {
        System.out.println("jdk 动态代理");
    }
}
