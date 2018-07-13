package facadePattern;

public class Test {
    public static void main(String[] args) {
        CPU cpu = new CPU();
        Memory memory = new Memory();
        Disk disk = new Disk();
        Computer computer = new Computer(cpu, disk, memory);
        computer.startup();
        computer.shutdown();
    }
}
