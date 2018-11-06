package market.seo.utils;

import market.seo.models.APP;
import market.seo.models.APPAnswer;
import market.seo.service.APPAnswerService;
import market.seo.service.AppService;
import org.springframework.util.StringUtils;

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
 * app应用
 */
@Named
public class APPAnswerUtil {
    @Inject
    private APPAnswerService appAnswerService;

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
            Integer[] indexArray = new Integer[14];
            String titles = "【标题开始】,【标题结束】,【内容开始】,【内容结束】,【关键词开始】,【关键词结束】,【内容2开始】,【内容2结束】,【标题2开始】,【标题2结束】,【内容3开始】,【内容3结束】,【URL开始】,【URL结束】";
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

    public int buildAPPAnswerAndSave(String scanFilePath) throws InterruptedException {
        final File root = new File(scanFilePath);
        List<File> fileList = new ArrayList<>();
        scanFile(root, fileList);
        int fileCount = fileList.size();
        CountDownLatch countDownLatch = new CountDownLatch(fileCount);
        int nThreads = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(1000);

        List<APPAnswer> collect = new ArrayList<>();
        for (int i = 0; i < fileCount; i += nThreads) {
            for (int j = i; j < i + nThreads; j++) {
                if (j < fileCount) {
                    File file = fileList.get(j);
                    Future<APPAnswer> dataFuture = executorService.submit(() -> createData(file));
                    try {
                        APPAnswer answer = dataFuture.get();
                        if (answer != null)
                            collect.add(answer);
                        countDownLatch.countDown();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        appAnswerService.save(collect);
        countDownLatch.await();

        return fileCount;
    }

    private static APPAnswer createData(File file) {
        String[] readToString = readToString(file);
        assert readToString != null;
        List<String> list = split(readToString[0], readToString[1], readToString[2]);
        if (list == null) System.out.println(file.getName());
        return list == null ? null : buildDataFromTxt(list);
    }

    public static void main(String[] args) {
        APPAnswer data = createData(new File("D:\\marketService\\zl\\seo文件\\APP问答\\APP问答\\【1】APP问答.txt"));
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

    private static APPAnswer buildDataFromTxt(List<String> dataList) {
        APPAnswer answer = new APPAnswer();
        answer.setTitle(hasText(dataList.get(2)) ? dataList.get(2) : dataList.get(6));
        answer.setContent(hasText(dataList.get(3)) ? dataList.get(3) : hasText(dataList.get(5)) ? dataList.get(5) : dataList.get(7));
        answer.setUrlHashCode(hasText(dataList.get(8)) ? dataList.get(8).trim().hashCode() : null);
        return answer;
    }

}
