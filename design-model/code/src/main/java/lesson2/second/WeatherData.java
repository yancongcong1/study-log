package lesson2.second;

import java.util.ArrayList;
import java.util.Observable;

/**
 * created by ycc at 2018\2\27 0027
 * 观察者，在此为气象站数据对象
 */
public class WeatherData extends Observable {

    private float temperature;
    private float humidity;
    private float pressure;

    public float getTemperature() {
        return temperature;
    }

    public float getHumidity() {
        return humidity;
    }

    public float getPressure() {
        return pressure;
    }

    public void setMeasurements(float temperature, float humidity, float pressure) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        measurementsChanged();
    }

    public void measurementsChanged() {
        // 更改被观察者状态
        setChanged();
        notifyObservers();
    }
}
