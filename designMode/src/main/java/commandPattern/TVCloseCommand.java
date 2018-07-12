package commandPattern;

public class TVCloseCommand implements Command {
    private TV tv;

    public TVCloseCommand(TV tv) {
        this.tv = tv;
    }

    @Override
    public void execute() {
        tv.closeTV();
    }
}
