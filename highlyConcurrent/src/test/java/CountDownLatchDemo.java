import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

public class CountDownLatchDemo {
    final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(2);
        Worker worker1 = new Worker("zhang san", 5000, latch);
        Worker worker2 = new Worker("li si", 3000, latch);
        worker1.start();
        worker2.start();
        latch.await();//等待所有工人完成工作
        System.out.println("all work done at "+sdf.format(new Date()));
    }

    private static class Worker extends Thread {
        private String workerName;
        private int sleepTime;
        private CountDownLatch latch;

        Worker(String workerName, int sleepTime, CountDownLatch latch) {
            this.workerName = workerName;
            this.sleepTime = sleepTime;
            this.latch = latch;
        }

        @Override
        public void run() {
            try {
                System.out.println("Worker " + workerName + " do work begin at " + sdf.format(new Date()));
                doWork();//工作了
                System.out.println("Worker " + workerName + " do work complete at " + sdf.format(new Date()));
            } finally {
                System.out.println(workerName+" before count "+latch.getCount());
                latch.countDown();//工人完成工作，计数器减一
                System.out.println(workerName+" after count "+latch.getCount());
            }
        }

        private void doWork() {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
