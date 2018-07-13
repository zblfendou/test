package adapterPattern.abstractAdapter;

import lombok.extern.log4j.Log4j;

@Log4j
public class SourceSub2 extends Wrapper2 {
    @Override
    public void method2() {
        log.debug("the sourceable interface's second Sub2!");
    }
}
