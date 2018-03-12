package lesson5.second;

/**
 * created by ycc at 2018\3\7 0007
 * 饿汉模式
 */
public class ChocolateBoiler1 {
    private static ChocolateBoiler1 instance = new ChocolateBoiler1();

    private ChocolateBoiler1() {

    }

    public static synchronized ChocolateBoiler1 getInstance() {
        return instance;
    }
}
