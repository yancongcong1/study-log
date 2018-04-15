package lesson8.components;

/**
 * created by ycc at 2018\3\20 0020
 * 列举部分需求步骤
 */
public class Dvd {
    public void on() {
        System.out.println("dvd is on");
    }

    public void play(String movie) {
        System.out.println("dvd " + movie + " is playing");
    }

    public void stop() {
        System.out.println("dvd is stopping");
    }

    public void eject() {
        System.out.println("dvd is ejecting");
    }

    public void off() {
        System.out.println("dvd is off");
    }
}
