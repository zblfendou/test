import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class CountDownLatchDemo1 {
    private static final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(4, 10, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    private static CountDownLatch cdl = new CountDownLatch(4);

    private static class GoThread extends Thread {
        private final String name;

        GoThread(String name) {
            this.name = name;
        }

        public void run() {
            System.out.println(name + "开始从宿舍出发");
            cdl.countDown();
            try {
                Thread.sleep(1000);
                cdl.await();
                System.out.println(name + "从楼底下出发");
                Thread.sleep(1000);
                System.out.println(name + "到达操场");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
    }

    public static void main(String[] args) {
        String[] str = {"李明", "王强", "刘凯", "赵杰"};
        for (int i = 0; i < 4; i++) {
            threadPool.execute(new GoThread(str[i]));
        }
        try {
            Thread.sleep(4000);
            System.out.println("四个人一起到达球场，现在开始打球");
            System.out.println("现在对CyclicBarrier进行复用.....");
            System.out.println("又来了一拨人，看看愿不愿意一起打：");


        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String[] str1 = {"王二", "洪光", "雷兵", "赵三"};
        //进行复用：
        for (int i = 0; i < 4; i++) {
            threadPool.execute(new GoThread(str1[i]));
        }
        try {
            Thread.sleep(4000);
            System.out.println("四个人一起到达球场，表示愿意一起打球，现在八个人开始打球");
            //System.out.println("现在对CyclicBarrier进行复用");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        threadPool.shutdown();

    }
}
