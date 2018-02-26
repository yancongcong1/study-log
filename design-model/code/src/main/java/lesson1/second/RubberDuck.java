package lesson1.second;

/**
 * created by ycc at 2018\2\26 0026
 * 橡皮鸭
 */
public class RubberDuck extends Duck implements Quackable, Flyable {
    public void fly() {
        System.out.println("can't fly");
    }

    public void quack() {
        System.out.println("gu gu");
    }
}
