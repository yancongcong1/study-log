package lesson5.second;

/**
 * created by ycc at 2018\3\7 0007
 * 双重锁模式(性能比懒汉模式好)
 */
public class ChocolateBoiler3 {
    private volatile static ChocolateBoiler3 instance;

    private ChocolateBoiler3() {

    }

    public static ChocolateBoiler3 getInstance() {
        if (instance == null) {
            synchronized (ChocolateBoiler3.class) {
                if (instance == null) {
                    instance = new ChocolateBoiler3();
                }
            }
        }
        return instance;
    }
}
