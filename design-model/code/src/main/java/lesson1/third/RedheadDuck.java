package lesson1.third;

/**
 * created by ycc at 2018\2\26 0026
 * 红头鸭
 */
public class RedheadDuck extends Duck {

    public RedheadDuck(FlyBehavior flyBehavior, QuackBehavior quackBehavior) {
        this.flyBehavior = flyBehavior;
        this.quackBehavior = quackBehavior;
    }

    public void display() {
        System.out.println("I'm redhead duck.");
    }
}
