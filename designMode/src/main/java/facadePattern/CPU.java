package facadePattern;

import lombok.extern.log4j.Log4j;

@Log4j
public class CPU implements ComputerAction{
    public void startup() {
      log.debug("cpu is startup !!!");
    }

    public void shutdown() {
        log.debug("cup is shutdown !!!");
    }
}
