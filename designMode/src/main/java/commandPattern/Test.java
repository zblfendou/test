package commandPattern;

public class Test {
    public static void main(String args[]){
        ElectricController electricController=new ElectricController();
        TV tv=new TV();
        Command tvOpenCommand=new TVOpenCommand(tv);
        Command tvCloseCommand=new TVCloseCommand(tv);
        electricController.setCommand(0,tvOpenCommand, tvCloseCommand);
        electricController.ONCommandButton(0);
        electricController.OFFCommandButton(0);
        Light light=new Light();
        Command lightOnCommand=new LightOpenCommand(light);
        Command lightOffCommand=new LightOffCommand(light);

        electricController.setCommand(1, lightOnCommand, lightOffCommand);
        electricController.ONCommandButton(1);
        electricController.OFFCommandButton(1);
    }

}
