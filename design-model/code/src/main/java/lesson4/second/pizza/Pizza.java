package lesson4.second.pizza;

import java.util.ArrayList;
import java.util.List;

/**
 * created by ycc at 2018\3\6 0006
 */
public abstract class Pizza {
    String name;
    String dough;
    String sauce;
    List<String> toppings = new ArrayList();

    public void prepare() {
        System.out.println(name + " prepared");
    }

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
