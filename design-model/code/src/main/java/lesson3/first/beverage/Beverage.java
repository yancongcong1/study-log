package lesson3.first.beverage;

/**
 * created by ycc at 2018\3\1 0001
 * 饮料的抽象类
 */
public abstract class Beverage {
    protected String description = "unknown beverage";

    public String getDescription() {
        return this.description;
    }

    public abstract double cost();
}
