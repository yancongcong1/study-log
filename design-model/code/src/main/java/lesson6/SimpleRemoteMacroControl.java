package lesson6;

import lesson6.command.Command;

/**
 * created by ycc at 2018\3\20 0020
 * 宏命令，一次调动一组命令
 */
public class SimpleRemoteMacroControl {
    Command[] slots;

    public SimpleRemoteMacroControl() {
    }

    public void setSlot(Command[] slots) {
        this.slots = slots;
    }

    public void buttonOnPressed() {
        for (Command slot : slots) {
            slot.execute();
        }
    }

    public void buttonOffPressed() {
        for (Command slot : slots) {
            slot.undo();
        }
    }
}
