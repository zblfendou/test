package market.seo.utils;

import market.seo.models.APPNews;
import market.seo.service.APPNewsService;

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
 * app 新闻
 */
@Named
public class APPNewsUtil {
    @Inject
    private APPNewsService appNewsService;

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
            Integer[] indexArray = new Integer[10];
            String titles = "【新闻标题开始】,【新闻标题结束】,【新闻内容开始】,【新闻内容结束】,【判断条件开始】,【判断条件结束】,【APP名称开始】,【APP名称结束】,【PageUrl开始】,【PageUrl结束】";
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

        List<APPNews> collect = new ArrayList<>();
        for (int i = 0; i < fileCount; i += nThreads) {
            for (int j = i; j < i + nThreads; j++) {
                if (j < fileCount) {
                    File file = fileList.get(j);
                    Future<APPNews> dataFuture = executorService.submit(() -> createData(file));
                    try {
                        APPNews appNews = dataFuture.get();
                        if (appNews != null)
                            collect.add(appNews);
                        countDownLatch.countDown();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        appNewsService.save(collect);
        countDownLatch.await();

        return fileCount;
    }

    private static APPNews createData(File file) {
        String[] readToString = readToString(file);
        assert readToString != null;
        List<String> list = split(readToString[0], readToString[1], readToString[2]);
        if (list == null) System.out.println(file.getName());
        return list == null ? null : buildDataFromTxt(list);
    }

    public static void main(String[] args) {
        APPNews data = createData(new File("D:\\marketService\\zl\\seo文件\\app\\news\\【216】APP新闻 - 第一页.txt"));
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

    private static APPNews buildDataFromTxt(List<String> dataList) {
        APPNews news = new APPNews();
        news.setTitle(dataList.get(2));
        news.setContent(dataList.get(3));
        news.setCondition_(dataList.get(4));
        news.setAppName(dataList.get(5));
        news.setPageUrl(dataList.get(6));
        return news;
    }

}
