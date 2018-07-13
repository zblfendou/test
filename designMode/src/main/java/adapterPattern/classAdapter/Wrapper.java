package adapterPattern.classAdapter;

import adapterPattern.Source;
import adapterPattern.Targetable;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@AllArgsConstructor
public class Wrapper implements Targetable {
    private Source source;
    @Override
    public void method1() {
        source.method1();
    }

    @Override
    public void method2() {
        log.debug("this is the targetable method");
    }
}
