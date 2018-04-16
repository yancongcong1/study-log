package lesson10;

import lesson10.menu.Menu;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * created by ycc at 2018\4\16 0016
 */
public class Waitress {
    ArrayList<Menu> menus = new ArrayList();

    public Waitress() {}

    public void addMenu(Menu menu) {
        this.menus.add(menu);
    }

    public void printMenu() {
        Iterator iterator = menus.iterator();
        while (iterator.hasNext()) {
            printMenu(((Menu) iterator.next()).getIterator());
        }
    }

    private void printMenu(Iterator iterator) {
        while (iterator.hasNext()) {
            MenuItem menuItem = (MenuItem) iterator.next();
            System.out.print(menuItem.getName() + ", ");
            System.out.print(menuItem.getDes() + ", ");
            System.out.println(menuItem.getPrice());
        }
    }
}
