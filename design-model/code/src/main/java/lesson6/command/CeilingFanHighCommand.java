package lesson6.command;

import lesson6.firm.CeilingFan;

/**
 * created by ycc at 2018\3\14 0014
 */
public class CeilingFanHighCommand implements Command {

    CeilingFan ceilingFan;
    int speed;

    public void setCeilingFan(CeilingFan ceilingFan) {
        this.ceilingFan = ceilingFan;
    }

    @Override
    public void execute() {
        this.speed = ceilingFan.getSpeed();
        ceilingFan.high();
    }

    @Override
    public void undo() {
        if (speed == CeilingFan.HIGH) {
            ceilingFan.high();
        } else if (speed == CeilingFan.MID) {
            ceilingFan.mid();
        } else if (speed == CeilingFan.LOW) {
            ceilingFan.low();
        } else {
            ceilingFan.off();
        }
    }
}
