package lesson4.second.pzfactory;

import lesson4.second.pizza.ChicagoCheesePizza;
import lesson4.second.pizza.ChicagoClamPizza;
import lesson4.second.pizza.Pizza;

/**
 * created by ycc at 2018\3\6 0006
 * new york pizza
 */
public class ChicagoPizzaFactory extends PizzaStore {
    public Pizza createPizza(String type) {
        Pizza pizza = null;

        if (type.equals("cheese")) {
            pizza = new ChicagoCheesePizza();
        } else if (type.equals("clam")) {
            pizza = new ChicagoClamPizza();
        }

        return pizza;
    }
}
