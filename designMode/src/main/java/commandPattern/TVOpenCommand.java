package commandPattern;

public class TVOpenCommand implements Command {
    private TV tv;

    public TVOpenCommand(TV tv) {
        this.tv = tv;
    }

    @Override
    public void execute() {
        tv.openTV();
    }
}
