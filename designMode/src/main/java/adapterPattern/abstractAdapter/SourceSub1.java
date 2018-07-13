package adapterPattern.abstractAdapter;

import lombok.extern.log4j.Log4j;

@Log4j
public class SourceSub1 extends Wrapper2 {
    @Override
    public void method1() {
      log.debug("the sourceable interface's first Sub1!");
    }
}
