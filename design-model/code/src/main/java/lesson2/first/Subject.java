package lesson2.first;

/**
 * created by ycc at 2018\2\27 0027
 * 观察者接口
 */
public interface Subject {
    void registerObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers();
}
