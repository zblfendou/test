package commandPattern;

import lombok.extern.log4j.Log4j;

@Log4j
public class NOCommand implements Command {
    @Override
    public void execute() {
        log.debug("暂时没有命令");
    }
}
