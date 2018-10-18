package market.seo.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import market.seo.models.AppComment;
import market.seo.vo.CommentDetail;
import market.seo.vo.Obj;
import market.seo.vo.Result;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class TaskUtil implements Callable<List<AppComment>> {

    private static final RestTemplate restTemplate;

    private static final ObjectMapper objectMapper;

    static {
        restTemplate = new RestTemplate();
        objectMapper = new ObjectMapper();
    }

    private String filePath;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
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

    private String readToString(File file) {
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
                    processData(firstUrl, apkName, page, contextData, commentDetails);
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

    private void processData(String firstUrl, String apkName, String page, String contextData, List<CommentDetail> list) throws IOException {
        String url = firstUrl + "&apkName=" + apkName + "&p=" + page + "&contextData=" + contextData;
        System.out.println(url);
        String result = restTemplate.getForObject(url, String.class);
        TypeReference<Result> typeReference = new TypeReference<Result>() {
        };
        Result re = objectMapper.readValue(result, typeReference);
        Obj obj = re.getObj();
        if (obj == null || obj.getCommentDetails().size() == 0) {
            if (obj != null && obj.getHasNext() == 1) {
                processData(firstUrl, apkName, page, obj.getContextData(), list);
            } else
                processData(firstUrl, apkName, page, contextData, list);
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
                            processData(firstUrl, apkName, page, contextData, list);
                        }
                    }
                }
            }
            if (list.size() > 9) return;
            processData(firstUrl, apkName, "2", obj.getContextData(), list);
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


    @Override
    public List<AppComment> call() throws Exception {
        return processFile(new File(filePath));
    }
}
