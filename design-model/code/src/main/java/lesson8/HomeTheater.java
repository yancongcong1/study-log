package lesson8;

import lesson8.components.Amplifier;
import lesson8.components.Dvd;

/**
 * created by ycc at 2018\3\20 0020
 * 家庭影院内容，只列举了部分步骤
 * 简化了接口，将客户从组件的子系统中解耦出来
 */
public class HomeTheater {
    Amplifier amplifier;
    Dvd dvd;

    public HomeTheater(Amplifier amplifier, Dvd dvd) {
        this.amplifier = amplifier;
        this.dvd = dvd;
    }

    public void watchMovie(String movie) {
        amplifier.on();
        amplifier.setDvd(dvd);
        dvd.on();
        dvd.play(movie);
    }

    public void endMovie() {
        amplifier.off();
        dvd.stop();
        dvd.eject();
        dvd.off();
    }
}
