package lesson10.iterator;

import lesson10.MenuItem;

import java.util.Iterator;

/**
 * created by ycc at 2018\4\16 0016
 */
public class DinnerIterator implements Iterator {
    MenuItem[] menuItems;
    int position = 0;

    public DinnerIterator(MenuItem[] menuItems) {
        this.menuItems = menuItems;
    }

    @Override
    public boolean hasNext() {
        if (position >= menuItems.length || menuItems[position] == null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public MenuItem next() {
        MenuItem menuItem = menuItems[position];
        position += 1;
        return menuItem;
    }

    @Override
    public void remove() {
        if (position <=0) {
            throw new IllegalStateException("error");
        }
        if (menuItems[position-1] != null) {
            for (int i = position-1; i < (menuItems.length-1); i++) {
                menuItems[i] = menuItems[i+1];
            }
            menuItems[menuItems.length-1] = null;
        }
    }
}
