package facadePattern;

import lombok.extern.log4j.Log4j;

@Log4j
public class Memory implements ComputerAction{
    public void startup() {
        log.debug("memory is startup !!!");
    }

    public void shutdown() {
        log.debug("memory is shutdown !!!");
    }
}
