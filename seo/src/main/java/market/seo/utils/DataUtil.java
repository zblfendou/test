package market.seo.utils;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import market.seo.models.Data;
import market.seo.service.DataService;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.util.StringUtils.hasText;

/**
 * 读取txt文件并将文件内容拼接成sql语句
 */
@Named
public class DataUtil {
    @Inject
    private DataService dataService;

    private static String[] readToString(File file) {
        String encoding = "UTF-8";
        String name = file.getName();
        String dataID = name.substring(name.indexOf("【") + 1, name.indexOf("】"));
        String taskID = name.substring(name.indexOf("采集任务") + "采集任务".length(), name.indexOf(".txt"));
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
            Integer[] indexArray = new Integer[12];
            String titles = "【标题开始】,【标题结束】,【标题2开始】,【标题2结束】,【内容开始】,【内容结束】,【内容2开始】,【内容2结束】,【内容3开始】,【内容3结束】,【关键词开始】,【关键词结束】";
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
            boolean dataRight = (hasText(list.get(2)) || hasText(list.get(3)))
                    && (list.get(4).length() > 0 || list.get(5).length() > 0 || list.get(6).length() > 0)
                    && hasText(list.get(7));
            if (dataRight) return list;
        }
        return null;
    }

    public int buildDataAndSave(String scanFilePath) throws InterruptedException {
        final File root = new File(scanFilePath);
        List<File> fileList = new ArrayList<>();
        scanFile(root, fileList);
        int fileCount = fileList.size();
        CountDownLatch countDownLatch = new CountDownLatch(fileCount);
        int nThreads = 10000;
        ExecutorService executorService = Executors.newFixedThreadPool(9);

        List<Data> collect = new ArrayList<>();
        for (int i = 0; i < fileCount; i += nThreads) {
            for (int j = i; j < i + nThreads; j++) {
                System.out.println(j);
                if (j < fileCount) {
                    File file = fileList.get(j);
                    Future<Data> dataFuture = executorService.submit(() -> createData(file));
                    try {
                        Data data = dataFuture.get();
                        if (data != null)
                            collect.add(data);
                        countDownLatch.countDown();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        dataService.save(collect);
        countDownLatch.await();

        return fileCount;
    }

    private static Data createData(File file) {
        String[] readToString = readToString(file);
        List<String> list = split(readToString[0], readToString[1], readToString[2]);
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

    private static Data buildDataFromTxt(List<String> dataList) {
        Data d = new Data();
        d.setDataID(Long.valueOf(dataList.get(0)));
        d.setTaskID(Long.valueOf(dataList.get(1)));
        d.setTitleOne(dataList.get(2));
        d.setTitleTwo(dataList.get(3));
        String contentOne = dataList.get(4);
        d.setContentOne(hasText(contentOne) ? contentOne.getBytes(UTF_8) : null);
        String contentTwo = dataList.get(5);
        d.setContentTwo(hasText(contentTwo) ? contentTwo.getBytes(UTF_8) : null);
        String contentThree = dataList.get(6);
        d.setContentThree(hasText(contentThree) ? contentThree.getBytes(UTF_8) : null);
        String keyword = dataList.get(7);
        d.setKeyword(keyword);
        return d;
    }

}
