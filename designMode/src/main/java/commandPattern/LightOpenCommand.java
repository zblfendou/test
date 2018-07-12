package commandPattern;

public class LightOpenCommand implements Command {
    private Light light;

    public LightOpenCommand(Light light) {
        this.light = light;
    }

    @Override
    public void execute() {
        light.openLight();
    }
}
