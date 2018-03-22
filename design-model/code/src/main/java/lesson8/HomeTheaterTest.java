package lesson8;

import lesson8.components.Amplifier;
import lesson8.components.Dvd;

/**
 * created by ycc at 2018\3\20 0020
 */
public class HomeTheaterTest {
    public static void main(String[] args) {
        HomeTheater homeTheater = new HomeTheater(new Amplifier(), new Dvd());
        homeTheater.watchMovie("地心历险记");
        System.out.println("==========================");
        homeTheater.endMovie();
    }
}
