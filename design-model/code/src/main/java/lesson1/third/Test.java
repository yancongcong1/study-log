package lesson1.third;

/**
 * created by ycc at 2018\2\26 0026
 */
public class Test {
    public static void main(String[] args) {
        MallardDuck mallardDuck = new MallardDuck(new FlyWithWings(), new Quack());
        RedheadDuck redheadDuck = new RedheadDuck(new FlyWithWings(), new Squeak());
        RubberDuck rubberDuck = new RubberDuck(new FlyNoWay(), new Squeak());

        redheadDuck.fly();
        redheadDuck.setFlyBehavior(new FlyNoWay());
        redheadDuck.fly();
    }
}
