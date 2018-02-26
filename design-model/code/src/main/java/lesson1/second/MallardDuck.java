package lesson1.second;

/**
 * created by ycc at 2018\2\26 0026
 * 绿头鸭
 */
public class MallardDuck extends Duck implements Quackable, Flyable {
    public void display() {
        System.out.println("I'm mallard duck.");
    }

    public void fly() {
        System.out.println("fly");
    }

    public void quack() {
        System.out.println("gua gua");
    }
}
