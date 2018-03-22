package lesson6.command;

import lesson6.firm.Light;

/**
 * created by ycc at 2018\3\12 0012
 */
public class LightOffCommand implements Command {
    private Light light;

    public LightOffCommand(Light light) {
        this.light = light;
    }

    public void execute() {
        light.off();
    }

    @Override
    public void undo() {
        light.on();
    }
}
