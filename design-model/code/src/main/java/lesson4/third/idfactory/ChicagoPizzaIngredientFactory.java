package lesson4.third.idfactory;

import lesson4.third.ingredient.Dough;
import lesson4.third.ingredient.PlumTomatoSauce;
import lesson4.third.ingredient.Sauce;
import lesson4.third.ingredient.ThickCrustDough;

/**
 * created by ycc at 2018\3\7 0007
 */
public class ChicagoPizzaIngredientFactory implements PizzaIngredientFactory {
    @Override
    public String getDescription() {
        return "chicago";
    }

    @Override
    public Dough createDough() {
        return new ThickCrustDough();
    }

    @Override
    public Sauce createSauce() {
        return new PlumTomatoSauce();
    }
}
