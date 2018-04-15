package lesson3.first.condiment;

import lesson3.first.beverage.Beverage;

/**
 * created by ycc at 2018\3\1 0001
 * 豆浆调料
 */
public class Soy extends CondimentDecorator {

    private Beverage beverage;

    public Soy(Beverage beverage) {
        this.beverage = beverage;
    }

    public String getDescription() {
        return beverage.getDescription() + ", soy";
    }

    public double cost() {
        return beverage.cost() + 0.15;
    }
}
