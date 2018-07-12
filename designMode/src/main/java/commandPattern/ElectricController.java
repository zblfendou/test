package commandPattern;

public class ElectricController {
    private Command[] onCommands = new Command[7];
    private Command[] offCommands = new Command[7];
    private Command undoCommand;

    public ElectricController() {
        for (int i = 0; i < 7; i++) {
            onCommands[i] = new NOCommand();
            offCommands[i] = new NOCommand();
        }
        undoCommand = new NOCommand();
    }

    public void setCommand(int pos,Command onCommand,Command offCommand){
        onCommands[pos]=onCommand;
        offCommands[pos]=offCommand;
    }
    public void removeCommand(int pos){
        offCommands[pos]=new NOCommand();
        onCommands[pos]=new NOCommand();
    }
    public void ONCommandButton(int pos){
        onCommands[pos].execute();
        undoCommand=onCommands[pos];
    }
    public void OFFCommandButton(int pos){
        offCommands[pos].execute();
        undoCommand=offCommands[pos];
    }
    public void undoButton(){//这个在接下来说明作用
        undoCommand.execute();
    }

}
