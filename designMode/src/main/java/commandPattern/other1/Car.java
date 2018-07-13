package commandPattern.other1;

import lombok.extern.log4j.Log4j;

@Log4j
public class Car {
    public void running() {
        log.debug("汽车跑起来了");
    }

    public void stop() {
        log.debug("汽车停下来了");
    }
}
