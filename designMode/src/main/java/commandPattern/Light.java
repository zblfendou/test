package commandPattern;

import lombok.extern.log4j.Log4j;

@Log4j
public class Light {
    public void openLight() {
        log.debug("开灯");
    }

    public void closeLight() {
        log.debug("关灯");
    }
}
