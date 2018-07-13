package adapterPattern.interfaceAdapter;

import adapterPattern.Source;
import adapterPattern.Targetable;
import lombok.extern.log4j.Log4j;

@Log4j
public class Adapter extends Source implements Targetable {
    @Override
    public void method2() {
      log.debug("this is the targetable method !!!");
    }
}
