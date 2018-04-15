package lesson5.second;

/**
 * created by ycc at 2018\3\7 0007
 * 懒汉模式(又饱汉模式，性能不好)
 */
public class ChocolateBoiler2 {
    private static ChocolateBoiler2 instance;

    private ChocolateBoiler2() {

    }

    public static synchronized ChocolateBoiler2 getInstance() {
        if (instance == null) {
            instance = new ChocolateBoiler2();
        }
        return instance;
    }
}
