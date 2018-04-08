import java.util.concurrent.Semaphore;

//Semaphore翻译成字面意思为 信号量，Semaphore可以控同时访问的线程个数，
// 通过 acquire() 获取一个许可，如果没有就等待，而 release() 释放一个许可
//public void acquire() throws InterruptedException {  } 用来获取一个许可，若无许可能够获得，则会一直等待，直到获得许可。
//public void acquire(int permits) throws InterruptedException { }    //获取permits个许可
//public void release() { }          用来释放许可。注意，在释放许可之前，必须先获获得许可。
//public void release(int permits) { }    //释放permits个许可
//这4个方法都会被阻塞，如果想立即得到执行结果，可以使用下面几个方法：
//public boolean tryAcquire() { };    //尝试获取一个许可，若获取成功，则立即返回true，若获取失败，则立即返回false
//public boolean tryAcquire(long timeout, TimeUnit unit) throws InterruptedException { };  //尝试获取一个许可，若在指定的时间内获取成功，则立即返回true，否则则立即返回false
//public boolean tryAcquire(int permits) { }; //尝试获取permits个许可，若获取成功，则立即返回true，若获取失败，则立即返回false
//public boolean tryAcquire(int permits, long timeout, TimeUnit unit) throws InterruptedException { }; //尝试获取permits个许可，若在指定的时间内获取成功，则立即返回true，否则则立即返回false
public class SemaphoreDemo {
    public static void main(String[] args) {
        int permits = 5;
        int workerNum = 8;
        Semaphore semaphore = new Semaphore(permits);
        for (int i = 0; i < workerNum; i++) {
            new Worker(i,semaphore).start();
        }
    }

    private static class Worker extends Thread{
        private int num;
        private Semaphore semaphore;
        Worker(int num, Semaphore semaphore) {
            this.num = num;
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            try {
                semaphore.acquire();
                System.out.println("工人"+this.num+"占用一个机器在生产...");
                Thread.sleep(3000);
                System.out.println("工人"+this.num+"释放出机器");
                semaphore.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
