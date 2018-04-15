package lesson7.first;

import lesson7.first.duck.Duck;
import lesson7.first.turkey.Turkey;

/**
 * created by ycc at 2018\3\20 0020
 * 火鸡适配器
 * 适配器的意图是将接口转换成不同的接口
 */
public class TurkeyAdapter implements Duck {
    Turkey turkey;

    public TurkeyAdapter(Turkey turkey) {
        this.turkey = turkey;
    }

    @Override
    public void quack() {
        turkey.gobble();
    }

    @Override
    public void fly() {
        for (int i = 0; i < 5; i++) {
            turkey.fly();
        }
    }
}
