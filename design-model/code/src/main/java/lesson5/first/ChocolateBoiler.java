package lesson5.first;

/**
 * created by ycc at 2018\3\7 0007
 * 存在bug的单例模式，在多线程下可能构造出多个对象
 */
public class ChocolateBoiler {
    private static ChocolateBoiler instance;

    private ChocolateBoiler() {

    }

    public static ChocolateBoiler getInstance() {
        if (instance == null) {
            instance = new ChocolateBoiler();
        }
        return instance;
    }
}
