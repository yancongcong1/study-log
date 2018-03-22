package lesson7.first.duck;

/**
 * created by ycc at 2018\3\20 0020
 */
public class MallardDuck implements Duck {
    @Override
    public void quack() {
        System.out.println("quack quack");
    }

    @Override
    public void fly() {
        System.out.println("flying");
    }
}
