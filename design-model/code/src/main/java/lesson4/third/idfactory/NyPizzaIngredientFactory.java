package lesson4.third.idfactory;

import lesson4.third.ingredient.Dough;
import lesson4.third.ingredient.ThinCrustDough;
import lesson4.third.ingredient.Sauce;
import lesson4.third.ingredient.MarinaraSauce;

/**
 * created by ycc at 2018\3\7 0007
 */
public class NyPizzaIngredientFactory implements PizzaIngredientFactory {
    @Override
    public String getDescription() {
        return "new york";
    }

    @Override
    public Dough createDough() {
        return new ThinCrustDough();
    }

    @Override
    public Sauce createSauce() {
        return new MarinaraSauce();
    }
}
