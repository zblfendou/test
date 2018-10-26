package market.seo.utils;

import market.seo.models.APPAnswer;
import market.seo.models.APPAnswerList;
import market.seo.service.APPAnswerListService;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.hasText;

/**
 * app应用
 */
@Named
public class APPAnswerListUtil {
    @Inject
    private APPAnswerListService appAnswerListService;

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
            ArrayList<String> list = new ArrayList<>();
            list.add(dataID);
            list.add(taskID);
            int kwdStartIndex = content.indexOf("【关键词开始】") + "【关键词开始】".length();
            int kwdEndIndex = content.indexOf("【关键词结束】");
            list.add(content.substring(kwdStartIndex, kwdEndIndex));
            int urlStartIndex = content.indexOf("【URL开始】") + "【URL开始】".length();
            int urlEndIndex = content.indexOf("###【分URL结束】【URL结束】");
            String splitContent = content.substring(urlStartIndex, urlEndIndex);
            if (splitContent.length() > 0) {
                String[] urlArrays = splitContent.split(" ###【分URL结束】【分URL开始】");
                Arrays.stream(urlArrays).forEach(url -> {
                    if (url.contains("【分URL结束】【分URL开始】")) url = url.replaceAll("【分URL结束】【分URL开始】", "");
                    list.add(String.valueOf(url.trim().hashCode()));
                });
                return list;
            }
        }
        return null;
    }

    public int buildAPPAnswerAndSave(String scanFilePath) throws InterruptedException {
        final File root = new File(scanFilePath);
        List<File> fileList = new ArrayList<>();
        scanFile(root, fileList);
        int fileCount = fileList.size();
        CountDownLatch countDownLatch = new CountDownLatch(fileCount);
        int nThreads = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(1000);

        List<APPAnswerList> collect = new ArrayList<>();
        for (int i = 0; i < fileCount; i += nThreads) {
            for (int j = i; j < i + nThreads; j++) {
                if (j < fileCount) {
                    File file = fileList.get(j);
                    Future<APPAnswerList> dataFuture = executorService.submit(() -> createData(file));
                    try {
                        APPAnswerList answerList = dataFuture.get();
                        if (answerList != null)
                            collect.add(answerList);
                        countDownLatch.countDown();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        appAnswerListService.save(collect);
        countDownLatch.await();

        return fileCount;
    }

    public static void main(String[] args) {
        String fileurl = "D:\\marketService\\zl\\seo文件\\APP问答\\列表\\【27931】APP问答 - 关键词及URL获取规则.txt";
        APPAnswerList data = createData(new File(fileurl));
        System.out.println(data);
    }

    private static APPAnswerList createData(File file) {
        String[] readToString = readToString(file);
        assert readToString != null;
        List<String> list = split(readToString[0], readToString[1], readToString[2]);
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

    private static APPAnswerList buildDataFromTxt(List<String> dataList) {
        APPAnswerList answerList = new APPAnswerList();
        answerList.setKeyword(dataList.get(2));
        List<String> list = dataList.subList(3, dataList.size());
        answerList.setList(list.stream().map(Integer::parseInt).collect(Collectors.toList()));
        return answerList;
    }

}
