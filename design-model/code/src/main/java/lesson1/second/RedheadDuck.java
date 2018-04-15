package lesson1.second;

/**
 * created by ycc at 2018\2\26 0026
 * 红头鸭
 */
public class RedheadDuck extends Duck implements Quackable, Flyable {
    public void display() {
        System.out.println("I'm redhead duck.");
    }

    public void fly() {
        System.out.println("fly");
    }

    public void quack() {
        System.out.println("gua gua");
    }
}
