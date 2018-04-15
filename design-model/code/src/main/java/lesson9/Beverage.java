package lesson9;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public abstract class Beverage {
    protected final void prepareRecipe() {
        boilWater();
        brew();
        pourInCup();
        if(customWantCondiments()) {
            addCondiments();
        }
    }

    private void boilWater() {
        System.out.println("boil water.");
    }

    protected void pourInCup() {
        System.out.println("pour in cup.");
    }

    private boolean customWantCondiments() {
        String answer = null;
        System.out.println("Are you want some condiments");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            answer = bufferedReader.readLine();
        } catch (Exception ex) {
            System.out.println("To error trying to read you answer.");
        }
        if (null == answer) {
            return false;
        } else if (answer.toLowerCase().startsWith("y")) {
            return true;
        }
        return false;
    }

    protected abstract void brew();

    protected abstract void addCondiments();
}
