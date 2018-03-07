package lesson3.first.condiment;

import lesson3.first.beverage.Beverage;

/**
 * created by ycc at 2018\3\1 0001
 * 奶泡调料
 */
public class Whip extends CondimentDecorator {

    private Beverage beverage;

    public Whip(Beverage beverage) {
        this.beverage = beverage;
    }

    public String getDescription() {
        return beverage.getDescription() + ", whip";
    }

    public double cost() {
        return beverage.cost() + 0.10;
    }
}
