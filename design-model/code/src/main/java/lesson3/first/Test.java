package lesson3.first;

import lesson3.first.beverage.Beverage;
import lesson3.first.beverage.Espresso;
import lesson3.first.condiment.Mocha;
import lesson3.first.condiment.Soy;
import lesson3.first.condiment.Whip;

/**
 * created by ycc at 2018\3\1 0001
 */
public class Test {

    public static void main(String[] args) {
        Beverage beverage = new Espresso();
        beverage = new Mocha(beverage);
        beverage = new Soy(beverage);
        beverage = new Whip(beverage);
        System.out.println(beverage.getDescription() + ": $" + beverage.cost());
    }
}
