package lesson6;

import lesson6.command.Command;

/**
 * created by ycc at 2018\3\14 0014
 * 遥控器
 */
public class SimpleRemoteControl {
    Command slot;

    public SimpleRemoteControl() {
    }

    public void setSlot(Command slot) {
        this.slot = slot;
    }

    public void buttonOnPressed() {
        slot.execute();
    }

    public void buttonOffPressed() {
        slot.undo();
    }
}
