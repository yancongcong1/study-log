package lesson4.second.pzfactory;

import lesson4.second.pizza.NyCheesePizza;
import lesson4.second.pizza.NyClamPizza;
import lesson4.second.pizza.Pizza;

/**
 * created by ycc at 2018\3\6 0006
 * new york pizza
 */
public class NyPizzaFactory extends PizzaStore {
    public Pizza createPizza(String type) {
        Pizza pizza = null;

        if (type.equals("cheese")) {
            pizza = new NyCheesePizza();
        } else if (type.equals("clam")) {
            pizza = new NyClamPizza();
        }

        return pizza;
    }
}
