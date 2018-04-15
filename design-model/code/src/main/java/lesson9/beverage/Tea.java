package lesson9.beverage;

import lesson9.Beverage;

public class Tea extends Beverage{
    protected void brew() {
        System.out.println("steeping the tea.");
    }

    protected void addCondiments() {
        System.out.println("adding lemon.");
    }
}
