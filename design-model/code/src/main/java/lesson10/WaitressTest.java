package lesson10;

import lesson10.menu.DinnerMenu;
import lesson10.menu.Menu;
import lesson10.menu.PancakeHouseMenu;

/**
 * created by ycc at 2018\4\16 0016
 */
public class WaitressTest {
    public static void main(String[] args) {
        Menu menu1 = new DinnerMenu();
        Menu menu2 = new PancakeHouseMenu();
        Waitress waitress = new Waitress();

        waitress.addMenu(menu1);
        waitress.addMenu(menu2);
        waitress.printMenu();
    }
}
