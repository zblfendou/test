/**
 * 单例模式测试用例
 */
public class SingletonTest2 {
    private static SingletonTest2 instance = null;

    private SingletonTest2() {
        System.out.println("SingletonTest2 is create ...");
    }

    private static class SingletonHolder {
        private static SingletonTest2 instance = new SingletonTest2();
    }

    public static SingletonTest2 getInstance() {
        return SingletonHolder.instance;
    }

    private void print() {
        System.out.println("singletonTest2 ...");
    }

    public static void main(String[] args) {
        getInstance().print();
    }
}
