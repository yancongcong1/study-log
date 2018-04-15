package lesson3.first.condiment;

import lesson3.first.beverage.Beverage;

/**
 * created by ycc at 2018\3\1 0001
 * 摩卡调料
 */
public class Mocha extends CondimentDecorator {

    Beverage beverage;

    public Mocha(Beverage beverage) {
        this.beverage = beverage;
    }

    public String getDescription() {
        return beverage.getDescription() + ", mocha";
    }

    public double cost() {
        return beverage.cost() + 0.20;
    }
}
