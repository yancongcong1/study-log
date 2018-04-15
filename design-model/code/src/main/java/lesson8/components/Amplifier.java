package lesson8.components;

/**
 * created by ycc at 2018\3\20 0020
 * 列举部分需求步骤
 */
public class Amplifier {
    public Dvd dvd;

    public void setDvd(Dvd dvd) {
        this.dvd = dvd;
    }

    public void on() {
        System.out.println("amp is on");
    }

    public void off() {
        System.out.println("amp is off");
    }
}
