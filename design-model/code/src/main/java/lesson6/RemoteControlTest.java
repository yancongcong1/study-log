package lesson6;

import lesson4.first.SimplePizzaFactory;
import lesson6.command.Command;
import lesson6.command.LightOnCommand;
import lesson6.firm.Light;

/**
 * created by ycc at 2018\3\14 0014
 */
public class RemoteControlTest {
    public static void main(String[] args) {
        SimpleRemoteControl simpleRemoteControl = new SimpleRemoteControl();

        Command command = new LightOnCommand(new Light());
        simpleRemoteControl.setSlot(command);
        simpleRemoteControl.buttonOnPressed();

        Command command1 = new LightOnCommand(new Light());
        simpleRemoteControl.setSlot(command1);
        simpleRemoteControl.buttonOnPressed();
        simpleRemoteControl.buttonOffPressed();
    }
}
