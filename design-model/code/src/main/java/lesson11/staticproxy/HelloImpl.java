package lesson11.staticproxy;

/**
 * Created on 2018\8\29 0029 by yancongcong
 */
public class HelloImpl implements Hello {

    @Override
    public void sayHello() {
        System.out.println("静态代理");
    }
}
