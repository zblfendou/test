package commandPattern;

import lombok.extern.log4j.Log4j;

@Log4j
public class TV {
    public void openTV() {
        log.debug("打开电视");
    }

    public void closeTV() {
        log.debug("关闭电视");
    }
}
