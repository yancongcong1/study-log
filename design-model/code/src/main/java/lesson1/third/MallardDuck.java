package lesson1.third;

/**
 * created by ycc at 2018\2\26 0026
 * 绿头鸭
 */
public class MallardDuck extends Duck {

    public MallardDuck(FlyBehavior flyBehavior, QuackBehavior quackBehavior) {
        this.flyBehavior = flyBehavior;
        this.quackBehavior = quackBehavior;
    }

    public void display() {
        System.out.println("I'm mallard duck.");
    }
}
