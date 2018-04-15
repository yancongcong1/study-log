package lesson7.first;

import lesson7.first.turkey.WildTurkey;

/**
 * created by ycc at 2018\3\20 0020
 */
public class DuckTestDriver {
    public static void main(String[] args) {
        TurkeyAdapter turkeyAdapter = new TurkeyAdapter(new WildTurkey());
        turkeyAdapter.quack();
        turkeyAdapter.fly();
    }
}
