package lesson2.first;

/**
 * created by ycc at 2018\2\27 0027
 */
public class Test {
    public static void main(String[] args) {
        WeatherData weatherData = new WeatherData();
        CurrentConditionsDisplay currentConditionsDisplay = new CurrentConditionsDisplay(weatherData);
        StatisticsDisplay statisticsDisplay = new StatisticsDisplay(weatherData);
        ForecastDisplay forecastDisplay = new ForecastDisplay(weatherData);

        weatherData.setMeasurements(80, 65, 20.1f);
        weatherData.setMeasurements(82, 66, 22.1f);
        weatherData.setMeasurements(83, 67, 23.1f);
    }
}
