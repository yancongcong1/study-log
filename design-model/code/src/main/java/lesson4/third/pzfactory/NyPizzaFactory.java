package lesson4.third.pzfactory;

import lesson4.third.idfactory.NyPizzaIngredientFactory;
import lesson4.third.idfactory.PizzaIngredientFactory;
import lesson4.third.pizza.CheesePizza;
import lesson4.third.pizza.ClamPizza;
import lesson4.third.pizza.Pizza;

/**
 * created by ycc at 2018\3\6 0006
 * new york pizza
 */
public class NyPizzaFactory extends PizzaStore {
    public Pizza createPizza(String type) {
        Pizza pizza = null;
        PizzaIngredientFactory pizzaIngredientFactory = new NyPizzaIngredientFactory();

        if (type.equals("cheese")) {
            pizza = new CheesePizza(pizzaIngredientFactory);
        } else if (type.equals("clam")) {
            pizza = new ClamPizza(pizzaIngredientFactory);
        }

        return pizza;
    }
}
