package market.seo.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import market.seo.daos.APPDao;
import market.seo.models.APP;
import market.seo.models.AppComment;
import market.seo.service.AppCommentService;
import market.seo.service.AppService;
import market.seo.vo.CommentDetail;
import market.seo.vo.Obj;
import market.seo.vo.Result;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Named
public class AppServiceImpl implements AppService {
    @Inject
    private AppCommentService appCommentService;
    @Inject
    private RestTemplate restTemplate;
    @Inject
    private ObjectMapper objectMapper;

    @Inject
    private APPDao appDao;

    @Override
    @Transactional
    public void save(List<APP> apps) {
        appDao.save(apps);
    }

    @Override
    public void test() {
        ArrayList<String> files = getFiles("d:\\app");
        ExecutorService executorService = Executors.newCachedThreadPool();

        int fileCount = files.size();
        int numbers = 1000;
        for (int i = 0; i < fileCount; i += numbers) {
            try {
                for (int j = i; j < i + numbers; j++) {
                    int finalJ = j;
                    Future<List<AppComment>> futures = executorService.submit(() -> processFile(new File(files.get(finalJ))));
                    List<AppComment> appComments = futures.get();
                    appCommentService.save(appComments);
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

        }
    }


    public static ArrayList<String> getFiles(String path) {
        ArrayList<String> files = new ArrayList<String>();
        File file = new File(path);
        File[] tempList = file.listFiles();

        assert tempList != null;
        for (File aTempList : tempList) {
            if (aTempList.isFile()) {
                files.add(aTempList.toString());
            }
        }
        return files;
    }

    public static void main(String[] args) {
        writeToFile(readToString(new File("d:\\1.txt")));
    }

    public static void writeToFile(String content) {
        String[] arrays = content.split(",");
        List<String> stringList = Arrays.asList(arrays);
        int size = stringList.size();
        int num = 1000;
        for (int i = 0; i < size; i += num) {
            List<String> list;
            if (i + num > size) {
                list = stringList.subList(i, size);
            } else {
                list = stringList.subList(i, i + num);
            }

            content = list.stream().collect(Collectors.joining(","));
            byte[] bytes = content.getBytes();
            File file = new File("d:\\app\\" + i + ".txt");
            try (FileOutputStream fop = new FileOutputStream(file)) {
                if (!file.exists()) {
                    file.createNewFile();
                }
                fop.write(bytes);
                fop.flush();
                fop.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private static String readToString(File file) {
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
            return new String(filecontent, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<AppComment> processFile(File file) throws InterruptedException {
        System.out.println("fileThread:>>" + Thread.currentThread().getName());
        String apkNameStrs = readToString(file);
        assert apkNameStrs != null;
        String[] apkNames = apkNameStrs.split(",");
        int apkCounts = apkNames.length;
        CountDownLatch countDownLatch = new CountDownLatch(apkCounts);
        int nThreads = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);

        ArrayList<AppComment> appComments = new ArrayList<>();
        for (int i = 0; i < apkCounts; i += nThreads) {
            for (int j = i; j < i + nThreads; j++) {
                if (j < apkCounts) {
                    String apkName = apkNames[j];
                    Future<AppComment> appCommentFuture = executorService.submit(() -> buildData(apkName));
                    AppComment appComment = null;
                    try {
                        appComment = appCommentFuture.get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    if (appComment != null) appComments.add(appComment);
                    countDownLatch.countDown();
                }
            }
        }
        countDownLatch.await();
        return appComments;
    }

    private AppComment buildData(String apkName) {
        System.out.println("apkThread >>" + Thread.currentThread().getName());
        String firstUrl = "https://sj.qq.com/myapp/app/comment.htm?";
        String page = "1";
        String contextData = "";
        boolean canGet = false;
        try {
            canGet = findCanGet(firstUrl, apkName, page, contextData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<CommentDetail> commentDetails = new ArrayList<>();
        if (canGet) {
            long before = System.currentTimeMillis();
            while (commentDetails.size() < 10) {
                System.out.println("apkName:" + apkName);
                try {
                    processData(firstUrl, apkName, page, contextData, commentDetails, System.currentTimeMillis());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                long after = System.currentTimeMillis();
                if (after - before > 10000) {
                    System.out.println("超时,跳出:" + apkName);
                    break;
                }
            }
        }
        if (commentDetails.size() == 0) return null;
        AppComment appComment = new AppComment();
        appComment.setApkName(apkName);
        String comment = null;
        try {
            comment = objectMapper.writeValueAsString(commentDetails);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        appComment.setComment(comment);
        return appComment;
    }

    private void processData(String firstUrl, String apkName, String page, String contextData, List<CommentDetail> list, long startTime) throws IOException {
        if (System.currentTimeMillis() - startTime > 10000) return;
        String url = firstUrl + "&apkName=" + apkName + "&p=" + page + "&contextData=" + contextData;
        System.out.println(url);
        String result = restTemplate.getForObject(url, String.class);
        TypeReference<Result> typeReference = new TypeReference<Result>() {
        };
        Result re = objectMapper.readValue(result, typeReference);
        Obj obj = re.getObj();
        if (obj == null || obj.getCommentDetails().size() == 0) {
            if (obj != null && obj.getHasNext() == 1) {
                processData(firstUrl, apkName, page, obj.getContextData(), list, startTime);
            } else
                processData(firstUrl, apkName, page, contextData, list, startTime);
        } else {
            List<CommentDetail> commentDetails = obj.getCommentDetails();
            for (CommentDetail commentDetail : commentDetails) {
                if (list.size() > 9) {
                    break;
                } else {
                    if (StringUtils.hasText(commentDetail.getNickName()) && StringUtils.hasText(commentDetail.getContent()))
                        list.add(commentDetail);
                    else {
                        if (obj.getHasNext() == 1) {
                            contextData = obj.getContextData();
                            processData(firstUrl, apkName, page, contextData, list, startTime);
                        }
                    }
                }
            }
            if (list.size() > 9) return;
            processData(firstUrl, apkName, "2", obj.getContextData(), list, startTime);
        }
    }

    private boolean findCanGet(String firstUrl, String apkName, String page, String contextData) throws IOException {
        String url = firstUrl + "&apkName=" + apkName + "&apkCode=212" + "&p=" + page + "&contextData=" + contextData;
        for (int i = 0; i < 30; i++) {
            String result = restTemplate.getForObject(url, String.class);
            TypeReference<Result> typeReference = new TypeReference<Result>() {
            };
            Result re = objectMapper.readValue(result, typeReference);
            Obj obj = re.getObj();
            if (obj != null && obj.getTotal() >= 10) return true;
        }
        return false;
    }

}
