package lesson3.first.condiment;

import lesson3.first.beverage.Beverage;

/**
 * created by ycc at 2018\3\1 0001
 * 调料的抽象类，继承自饮料
 */
public abstract class CondimentDecorator extends Beverage {
    public abstract String getDescription();
}
