import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicTest {

    public static void main(String[] args) {
        final BlockingQueue<File> queue = new LinkedBlockingDeque<>(5000);
        final ExecutorService exec = Executors.newFixedThreadPool(8);
        final File root = new File("D:\\");
        final File exitFile = new File("");
        final AtomicInteger rc = new AtomicInteger();
        final AtomicInteger wc = new AtomicInteger();
            final Runnable read = new Runnable() {
                @Override
                public void run() {
                    scanFile(root);
                    scanFile(exitFile);
                }

                private void scanFile(File file) {
                    if (file.isDirectory()) {
                        final File[] files = file.listFiles(pathname -> pathname.isDirectory() || pathname.getPath().endsWith(".java"));
                        for (File one : files != null ? files : new File[0]) {
                            scanFile(one);
                        }
                    } else {
                        try {
                            final int index = rc.incrementAndGet();
                            System.out.println("read0:" + index + "  " + file.getPath());
                            queue.put(file);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            exec.submit(read);

        for (int index = 0; index < 7; index++) {
            final int num = index;
            final Runnable write = new Runnable() {
                String threadName = "Write" + num;

                @Override
                public void run() {
                    while (true) {
                        try {
                            final int index = wc.incrementAndGet();
                            final File file = queue.take();
                            if (file == exitFile) {
                                queue.put(exitFile);
                                break;
                            }
                            System.out.println(threadName + ":" + index + " " + file.getPath());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }
            };
            exec.submit(write);
        }
        exec.shutdown();
    }
}
