package lesson4.second;

import lesson4.second.pzfactory.ChicagoPizzaFactory;
import lesson4.second.pzfactory.NyPizzaFactory;
import lesson4.second.pzfactory.PizzaStore;

/**
 * created by ycc at 2018\3\6 0006
 */
public class Test {
    public static void main(String[] args) {
        PizzaStore nyStore = new NyPizzaFactory();
        PizzaStore chicago = new ChicagoPizzaFactory();

        nyStore.orderPizza("cheese");
        System.out.println();
        System.out.println("--------------------");
        System.out.println();
        chicago.orderPizza("cheese");
    }
}
