package adapterPattern.classAdapter;

import adapterPattern.Source;

public class Test {
    public static void main(String[] args) {
        Source source = new Source();
        Wrapper wrapper = new Wrapper(source);
        wrapper.method1();wrapper.method2();
    }
}
