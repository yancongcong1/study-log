package lesson10.menu;

import lesson10.MenuItem;
import lesson10.iterator.DinnerIterator;

import java.util.Iterator;

/**
 * created by ycc at 2018\4\16 0016
 */
public class DinnerMenu implements Menu {
    static final int MAX_ITEMS = 6;
    int numberOfItem = 0;
    MenuItem[] menuItems;

    public DinnerMenu() {
        menuItems = new MenuItem[MAX_ITEMS];
        addItem("Vegetarian BLT", "Fakin bacon with lettuce", true, 2.99);
        addItem("BLT", "Bacon with lettuce ", false, 2.99);
        addItem("Soup of the day", "Soup of the day", true, 3.49);
    }

    public void addItem(String name, String des, boolean vegetarian, double price) {
        MenuItem menuItem = new MenuItem(name, des, vegetarian, price);
        if (numberOfItem >= MAX_ITEMS) {
            System.out.println("Can't add item because it is full");
        } else {
            menuItems[numberOfItem] = menuItem;
            numberOfItem += 1;
        }
    }

    public Iterator<MenuItem> getIterator() {
        return new DinnerIterator(menuItems);
    }
}
