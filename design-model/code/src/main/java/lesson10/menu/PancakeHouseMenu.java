package lesson10.menu;

import lesson10.MenuItem;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * created by ycc at 2018\4\16 0016
 */
public class PancakeHouseMenu implements Menu {
    ArrayList<MenuItem> menuItems;

    public PancakeHouseMenu() {
        menuItems = new ArrayList();
        addItem("K&B's Pancake Breakfast", "Pancakes with scrambled eggs, toast", true, 2.99);
        addItem("Regular Pancake Breakfast", "Pancakes with fried eggs, sausage", false, 2.99);
        addItem("Blueberry Pancakes", "Pancakes made with fresh blueberries", true, 3.49);
    }

    public void addItem(String name, String des, boolean vegetarian, double price) {
        MenuItem menuItem = new MenuItem(name, des, vegetarian, price);
        menuItems.add(menuItem);
    }

    public Iterator getIterator() {
        return menuItems.iterator();
    }
}
