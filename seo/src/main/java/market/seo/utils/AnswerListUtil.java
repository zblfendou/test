package market.seo.utils;

import market.seo.models.AnswerList;
import market.seo.models.Data;
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
public class AnswerListUtil {
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
            ArrayList<String> list = new ArrayList<>();
            list.add(dataID);
            String keyword = content.substring(content.indexOf("【关键词开始】") + "【关键词开始】".length(), content.indexOf("【关键词结束】"));
            list.add(keyword);
            String urlContent = content.substring(content.indexOf("【URL开始】") + "【URL开始】".length(), content.lastIndexOf("【URL结束】"));
            if (urlContent.length() > "【分URL结束】【分URL开始】".length()) {
                urlContent = urlContent.substring("【分URL结束】【分URL开始】".length(), urlContent.length() - "###【分URL结束】".length());
                String[] urlList = urlContent.split("###【分URL结束】【分URL开始】");
                List<String> listHashCode = Arrays.stream(urlList).map(url -> {
                    if (url != null && url.length() > 0) {
                        return String.valueOf(url.trim().hashCode());
                    } else return null;
                }).filter(Objects::nonNull).collect(Collectors.toList());
                list.addAll(listHashCode);
            }
            return list;
        }
        return null;
    }

    public static void main(String[] args) {
        createData(new File("D:\\marketService\\zl\\seo文件\\问答\\列表\\1\\【35】百度知道 - 关键词及URL获取规则 - 第一页.txt"));
    }

    public int buildDataAndSave(String scanFilePath) throws InterruptedException {
        final File root = new File(scanFilePath);
        List<File> fileList = new ArrayList<>();
        scanFile(root, fileList);
        int fileCount = fileList.size();
        CountDownLatch countDownLatch = new CountDownLatch(fileCount);
        int nThreads = 10000;
        ExecutorService executorService = Executors.newFixedThreadPool(1000);

        List<AnswerList> collect = new ArrayList<>();
        for (int i = 0; i < fileCount; i += nThreads) {
            for (int j = i; j < i + nThreads; j++) {
                if (j < fileCount) {
                    File file = fileList.get(j);
                    Future<AnswerList> dataFuture = executorService.submit(() -> createData(file));
                    try {
                        AnswerList data = dataFuture.get();
                        if (data != null)
                            collect.add(data);
                        countDownLatch.countDown();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        service.save(collect);
        countDownLatch.await();

        return fileCount;
    }

    private static AnswerList createData(File file) {
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

    private static AnswerList buildDataFromTxt(List<String> dataList) {
        AnswerList a = new AnswerList();
        a.setDataID(Integer.parseInt(dataList.get(0)));
        a.setKeyword(dataList.get(1));
        List<String> strHashCodeList = dataList.subList(2, dataList.size());
        List<Integer> collect = strHashCodeList.stream().map(Integer::parseInt).collect(Collectors.toList());
        a.setListHashCode(collect);
        return a;
    }

}
