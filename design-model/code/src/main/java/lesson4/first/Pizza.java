package lesson4.first;

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

    void prepare() {
        System.out.println("prepared");
    }

    void bake() {
        System.out.println("baked");
    }

    void cut() {
        System.out.println("cut");
    }

    void box() {
        System.out.println("boxed");
    }
}
