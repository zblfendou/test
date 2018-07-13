package adapterPattern;

import lombok.extern.log4j.Log4j;

@Log4j
public class Source {
    public void method1() {
        log.debug("this is original method");
    }
}
