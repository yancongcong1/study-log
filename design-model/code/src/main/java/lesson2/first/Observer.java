package lesson2.first;

/**
 * created by ycc at 2018\2\27 0027
 * 订阅者接口
 */
public interface Observer {
    void update(float temp, float humidity, float pressure);
}
