package lesson9.beverage;

import lesson9.Beverage;

public class Coffee extends Beverage {
    protected void brew() {
        System.out.println("steeping the water.");
    }

    protected void addCondiments() {
        System.out.println("adding sugar and milk.");
    }
}
