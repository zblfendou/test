/**
 * 单例模式测试用例
 */
public class SingletonTest1 {
    private static SingletonTest1 instance = null;

    private SingletonTest1() {
        System.out.println("singleton is create ...");
    }

    private static synchronized void getInstance() {
        if (instance == null) instance = new SingletonTest1();
    }

    private static void print() {
        System.out.println("singletonTest2 ...");
    }

    public static void main(String[] args) {
        getInstance();
    }
}
