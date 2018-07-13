package facadePattern;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@AllArgsConstructor
public class Computer implements ComputerAction {
    private CPU cpu;
    private Disk disk;
    private Memory memory;


    @Override
    public void startup() {
        log.debug("start the computer !!!");
        cpu.startup();
        memory.startup();
        disk.startup();
        log.debug("start the computer finished !!!");
    }

    @Override
    public void shutdown() {
        log.debug("shutdown the computer !!!");
        cpu.shutdown();
        memory.shutdown();
        disk.shutdown();
        log.debug("shutdown the computer finished !!!");
    }
}
