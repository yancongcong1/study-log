package lesson6.firm;

/**
 * created by ycc at 2018\3\14 0014
 * 电扇
 */
public class CeilingFan {
    public static final int OFF = 0;
    public static final int LOW = 1;
    public static final int MID = 2;
    public static final int HIGH = 3;
    String location;
    int speed;

    public CeilingFan(String location, int speed) {
        this.location = location;
        this.speed = speed;
    }

    public int getSpeed() {
        return speed;
    }

    public void off() {
        this.speed = OFF;
    }

    public void low() {
        this.speed = LOW;
    }

    public void mid() {
        this.speed = MID;
    }

    public void high() {
        this.speed = HIGH;
    }
}
