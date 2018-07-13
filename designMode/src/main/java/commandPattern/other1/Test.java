package commandPattern.other1;

public class Test {
    public static void main(String[] args) {
        Car car = new Car();
        RunningCommand runningCommand = new RunningCommand(car);
        runningCommand.actionExecutor();
        StopCommand stopCommand = new StopCommand(car);
        stopCommand.actionExecutor();
    }
}
