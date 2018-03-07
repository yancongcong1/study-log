package lesson4.third.pzfactory;

import lesson4.third.pizza.Pizza;

/**
 * created by ycc at 2018\3\6 0006
 * 使用抽象对象的方式(工厂模式)
 */
public abstract class PizzaStore {

    public Pizza orderPizza(String type) {

        Pizza pizza = createPizza(type);

        pizza.prepare();
        pizza.bake();
        pizza.cut();
        pizza.bake();
        return pizza;
    }

    public abstract Pizza createPizza(String type);
}
