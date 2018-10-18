package market.seo.utils;

import market.seo.models.APP;
import market.seo.service.AppService;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.springframework.util.StringUtils.hasText;

/**
 * 读取txt文件并将文件内容拼接成sql语句
 */
@Named
public class APPUtil {
    @Inject
    private AppService appService;

    private static String[] readToString(File file) {
        String encoding = "UTF-8";
        String name = file.getName();
        String dataID = name.substring(name.indexOf("【") + 1, name.indexOf("】"));
        String taskID = "1111";
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String[]{new String(filecontent, encoding), dataID, taskID};
        } catch (UnsupportedEncodingException e) {
            System.err.println("The OS does not support " + encoding);
            e.printStackTrace();
            return null;
        }
    }

    private static List<String> split(String content, String dataID, String taskID) {
        if (hasText(content)) {
            Integer[] indexArray = new Integer[18];
            String titles = "【APP标题开始】,【APP标题结束】,【APP评分开始】,【APP评分结束】,【APP分类开始】,【APP分类结束】,【APP下载量开始】,【APP下载量结束】,【二维码地址开始】,【二维码地址结束】," +
                    "【APP封面地址开始】,【APP封面地址结束】,【APP简介开始】,【APP简介结束】,【APPlogo地址】,【APPlogo地址结束】,【APP链接开始】,【APP链接结束】";
            String[] titleSplits = titles.split(",");
            for (int i = 0; i < titleSplits.length - 1; i += 2) {
                indexArray[i] = content.indexOf(titleSplits[i]) + titleSplits[i].length();
                indexArray[i + 1] = content.indexOf(titleSplits[i + 1]);
            }

            ArrayList<String> list = new ArrayList<>();
            list.add(dataID);
            list.add(taskID);
            for (int i = 0; i < indexArray.length; i += 2) {
                String substring = content.substring(indexArray[i], indexArray[i + 1]);
                list.add(substring);
            }
//            boolean dataRight = (hasText(list.get(2)) || hasText(list.get(3)))
//                    && (list.get(4).length() > 0 || list.get(5).length() > 0 || list.get(6).length() > 0)
//                    && hasText(list.get(7));
//            int titleHashCode = (list.get(2) + list.get(3)).hashCode();
//            int contentHashCode = (list.get(4)+list.get(5)+list.get(6)).hashCode();
//            list.add(String.valueOf(titleHashCode));
//            list.add(String.valueOf(contentHashCode));
//            if (dataRight) return list;
            return list;
        }
        return null;
    }

    public int buildAPPAndSave(String scanFilePath) throws InterruptedException {
        final File root = new File(scanFilePath);
        List<File> fileList = new ArrayList<>();
        scanFile(root, fileList);
        int fileCount = fileList.size();
        CountDownLatch countDownLatch = new CountDownLatch(fileCount);
        int nThreads = 10000;
        ExecutorService executorService = Executors.newFixedThreadPool(1000);

        List<APP> collect = new ArrayList<>();
        for (int i = 0; i < fileCount; i += nThreads) {
            for (int j = i; j < i + nThreads; j++) {
                if (j < fileCount) {
                    File file = fileList.get(j);
                    Future<APP> dataFuture = executorService.submit(() -> createData(file));
                    try {
                        APP app = dataFuture.get();
                        if (app != null)
                            collect.add(app);
                        countDownLatch.countDown();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        appService.save(collect);
        countDownLatch.await();

        return fileCount;
    }

    private static APP createData(File file) {
        String[] readToString = readToString(file);
        assert readToString != null;
        List<String> list = split(readToString[0], readToString[1], readToString[2]);
        if (list == null) System.out.println(file.getName());
        return list == null ? null : buildDataFromTxt(list);
    }

    public static void main(String[] args) {
        APP data = createData(new File("D:\\marketService\\zl\\seo文件\\app\\详情页1-48000\\【72015】应用宝1-48000-1.txt"));
        System.out.println(data);
    }

    private static void scanFile(File file, List<File> fileList) {
        if (file.isDirectory()) {
            final File[] files = file.listFiles(pathname -> pathname.isDirectory() || pathname.getPath().endsWith(".txt"));
            for (File one : files != null ? files : new File[0]) {
                scanFile(one, fileList);
            }
        } else {
            fileList.add(file);
        }
    }

    private static APP buildDataFromTxt(List<String> dataList) {
        APP app = new APP();
        app.setTitle(dataList.get(2));
        app.setScore(dataList.get(3));
        app.setClassify(dataList.get(4));
        app.setDownloadCount(dataList.get(5));
        app.setQrCodeAddress(dataList.get(6));
        app.setCoverAddress(dataList.get(7));
        app.setDescribution(dataList.get(8));
        app.setLogoAddress(dataList.get(9));
        app.setLink(dataList.get(10));
        return app;
    }

}
