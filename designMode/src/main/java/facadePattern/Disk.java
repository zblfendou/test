package facadePattern;

import lombok.extern.log4j.Log4j;

@Log4j
public class Disk implements ComputerAction{
    public void startup() {
        log.debug("disk is startup !!!");
    }

    public void shutdown() {
        log.debug("disk is shutdown !!!");
    }
}
