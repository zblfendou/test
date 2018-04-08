import java.util.concurrent.CountDownLatch;

public class CountDownLatchDemo01 {
    private static CountDownLatch countDownLatch = new CountDownLatch(2);

    public static void main(String[] args) throws InterruptedException {

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("1");
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
                System.out.println("2");
                try {
                    Thread.sleep(3500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();

            }
        }).start();
        countDownLatch.await();
        System.out.println("结束");

    }
}
