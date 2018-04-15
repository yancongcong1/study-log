package lesson4.third.pizza;

import lesson4.third.ingredient.Dough;
import lesson4.third.ingredient.Sauce;

import java.util.ArrayList;
import java.util.List;

/**
 * created by ycc at 2018\3\6 0006
 */
public abstract class Pizza {
    String name;
    Dough dough;
    Sauce sauce;

    public abstract void prepare();

    public void bake() {
        System.out.println("baked");
    }

    public void cut() {
        System.out.println("cut");
    }

    public void box() {
        System.out.println("boxed");
    }
}
