package lesson4.third.idfactory;

import lesson4.third.ingredient.Dough;
import lesson4.third.ingredient.Sauce;

/**
 * created by ycc at 2018\3\7 0007
 * 每个类都是原料
 */
public interface PizzaIngredientFactory {
    String getDescription();
    Dough createDough();
    Sauce createSauce();
    // ...省略许多类
}
