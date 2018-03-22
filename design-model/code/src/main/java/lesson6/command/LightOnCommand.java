package lesson6.command;

import lesson6.firm.Light;

/**
 * created by ycc at 2018\3\12 0012
 */
public class LightOnCommand implements Command {
    private Light light;

    public LightOnCommand(Light light) {
        this.light = light;
    }

    public void execute() {
        light.on();
    }

    @Override
    public void undo() {
        light.off();
    }
}
