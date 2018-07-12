package commandPattern;

public class NOCommand implements Command {
    @Override
    public void execute() {
        System.out.println("暂时没有命令");
    }
}
