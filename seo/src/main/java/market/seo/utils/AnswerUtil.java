package market.seo.utils;

import market.seo.models.Answer;
import market.seo.models.AnswerList;
import market.seo.service.AnswerService;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.hasText;

/**
 * 读取txt文件并将文件内容拼接成sql语句
 */
@Named
public class AnswerUtil {
    @Inject
    private AnswerService service;

    private static String[] readToString(File file) {
        String encoding = "UTF-8";
        String name = file.getName();
        String dataID = name.substring(name.indexOf("【") + 1, name.indexOf("】"));
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
            return new String[]{new String(filecontent, encoding), dataID};
        } catch (UnsupportedEncodingException e) {
            System.err.println("The OS does not support " + encoding);
            e.printStackTrace();
            return null;
        }
    }

    private static List<String> split(String content, String dataID) {
        if (hasText(content)) {
            Integer[] indexArray = new Integer[18];
            String titles = "【URL开始】,【URL结束】,【标题开始】,【标题结束】,【标题2开始】,【标题2结束】,【内容开始】,【内容结束】,【内容2开始】,【内容2结束】,【内容3开始】,【内容3结束】," +
                    "【内容4开始】,【内容4结束】,【内容5开始】,【内容5结束】,【内容6开始】,【内容6结束】";
            String[] titleSplits = titles.split(",");
            for (int i = 0; i < titleSplits.length - 1; i += 2) {
                indexArray[i] = content.indexOf(titleSplits[i]) + titleSplits[i].length();
                indexArray[i + 1] = content.indexOf(titleSplits[i + 1]);
            }

            ArrayList<String> list = new ArrayList<>();
            list.add(dataID);
            for (int i = 0; i < indexArray.length; i += 2) {
                String substring = content.substring(indexArray[i], indexArray[i + 1]);
                list.add(substring);
            }
            boolean dataRight = (hasText(list.get(2)) || hasText(list.get(3)))
                    && (hasText(list.get(4)) || hasText(list.get(5)) ||
                    hasText(list.get(6)) || hasText(list.get(7)) ||
                    hasText(list.get(8)) || hasText(list.get(9)));
            if (dataRight) return list;
        }
        return null;
    }

    public static void main(String[] args) {
        createData(new File("D:\\marketService\\zl\\seo文件\\问答\\内容页\\内容页1\\【1】知道内容采集-1.txt"));
    }

    public int buildDataAndSave(String scanFilePath) throws InterruptedException {
        final File root = new File(scanFilePath);
        List<File> fileList = new ArrayList<>();
        scanFile(root, fileList);
        int fileCount = fileList.size();
        CountDownLatch countDownLatch = new CountDownLatch(fileCount);
        int nThreads = 10000;
        ExecutorService executorService = Executors.newFixedThreadPool(1000);

        List<Answer> collect = new ArrayList<>();
        for (int i = 0; i < fileCount; i += nThreads) {
            for (int j = i; j < i + nThreads; j++) {
                if (j < fileCount) {
                    File file = fileList.get(j);
                    Future<Answer> dataFuture = executorService.submit(() -> createData(file));
                    try {
                        Answer data = dataFuture.get();
                        if (data != null)
                            collect.add(data);
                        countDownLatch.countDown();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        service.saveAnswers(collect);
        countDownLatch.await();

        return fileCount;
    }

    private static Answer createData(File file) {
        String[] readToString = readToString(file);
        assert readToString != null;
        List<String> list = split(readToString[0], readToString[1]);
        if (list == null) System.out.println(file.getName());
        return list == null ? null : buildDataFromTxt(list);
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

    private static Answer buildDataFromTxt(List<String> dataList) {
        Answer a = new Answer();
        a.setHashCode(dataList.get(1).trim().hashCode());
        a.setTitle(hasText(dataList.get(2)) ? dataList.get(2) : dataList.get(3));
        a.setContent(hasText(dataList.get(4)) ? dataList.get(4) : hasText(dataList.get(5)) ? dataList.get(5) : hasText(dataList.get(6)) ? dataList.get(6) : hasText(dataList.get(7)) ? dataList.get(7) : hasText(dataList.get(8)) ? dataList.get(8) : dataList.get(9));
        return a;
    }

}
