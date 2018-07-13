package commandPattern.other1;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@AllArgsConstructor
public class StopCommand implements ActionCommand {
    private Car car;

    @Override
    public void actionExecutor() {
        car.stop();
    }
}
