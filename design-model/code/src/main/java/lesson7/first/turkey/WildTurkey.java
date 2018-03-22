package lesson7.first.turkey;

/**
 * created by ycc at 2018\3\20 0020
 */
public class WildTurkey implements Turkey {
    @Override
    public void gobble() {
        System.out.println("gobble gobble");
    }

    @Override
    public void fly() {
        System.out.println("fly a short distance");
    }
}
