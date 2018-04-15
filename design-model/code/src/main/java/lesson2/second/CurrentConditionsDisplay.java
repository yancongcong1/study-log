package lesson2.second;

import java.util.Observable;
import java.util.Observer;

/**
 * created by ycc at 2018\2\27 0027
 */
public class CurrentConditionsDisplay implements Observer, DisplayElement {

    private float temperature;
    private float humidity;
    private float pressure;

    private Observable observable;

    public CurrentConditionsDisplay(Observable observable) {
        this.observable = observable;
        observable.addObserver(this);
    }

    public void update(Observable o, Object arg) {
        if (o instanceof WeatherData) {
            if (arg == null) {
                WeatherData weatherData = (WeatherData) o;
                this.temperature = weatherData.getTemperature();
                this.humidity = weatherData.getHumidity();
                this.pressure = weatherData.getPressure();
            } else {
                // 解析arg对象
            }
            display();
        }
    }

    public void display() {
        System.out.println("temperature:" + this.temperature + " humidity:" + this.humidity + " pressure:" + this.pressure);
    }
}
