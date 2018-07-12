package decoratorPattern;

public class Employee implements Person {
    @Override
    public void doCoding() {
        System.out.println("程序员加班写程序呀,写呀写,终于写完了...");
    }
}
