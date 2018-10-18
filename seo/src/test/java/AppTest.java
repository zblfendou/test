import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import market.seo.daos.AppCommentDao;
import market.seo.models.AppComment;
import market.seo.service.AppCommentService;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import seo.market.baseTester;
import seo.market.vo.CommentDetail;
import seo.market.vo.Obj;
import seo.market.vo.Result;

import javax.annotation.Resource;
import javax.inject.Inject;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
public class AppTest extends baseTester {
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private ObjectMapper objectMapper;
    @Inject
    private AppCommentService appCommentService;

    @Test
    @Rollback(false)
    public void testGet() throws InterruptedException {

        ArrayList<String> files = getFiles("d:\\app");
        ExecutorService executorService = Executors.newCachedThreadPool();
        ArrayList<Task> tasks = new ArrayList<>();
        files.forEach(filePath -> tasks.add(new Task(filePath)));

        List<Future<String>> futures = executorService.invokeAll(tasks);
        for (Future<String> future : futures) {
            try {
                System.out.println(future.get());
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }




    public static ArrayList<String> getFiles(String path) {
        ArrayList<String> files = new ArrayList<>();
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



    public class Task implements Callable<String> {
        private String filePath;

        public Task(String filePath) {
            this.filePath = filePath;
        }

        private String processFile(File file) throws InterruptedException {
            System.out.println("fileThread:>>" + Thread.currentThread().getName());
            String apkNameStrs = readToString(file);
            assert apkNameStrs != null;
            String[] apkNames = apkNameStrs.split(",");
            int apkCounts = apkNames.length;
            CountDownLatch countDownLatch = new CountDownLatch(apkCounts);
            int nThreads = 1000;
            ExecutorService executorService = Executors.newFixedThreadPool(nThreads);

            for (int i = 0; i < apkCounts; i += nThreads) {
                ArrayList<AppComment> appComments = new ArrayList<>();
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
                System.out.println("大小:" + appComments.size());
                appCommentService.save(appComments);
            }
            countDownLatch.await();
            return "process is finished";
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

        @Test
        public void testWriteFile() {
            File file = new File("d:\\1.txt");
            writeToFile(readToString(file));
        }

        public void writeToFile(String content) {
            String[] arrays = content.split(",");
            List<String> stringList = Arrays.asList(arrays);
            int size = stringList.size();
            int num = 1000;
            for (int i = 0; i < size; i += num) {
                List<String> list;
                if (i + num > size) {
                    list = stringList.subList(i, size);
                } else {
                    list = stringList.subList(0, i + num);
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
                } finally {
                }
            }

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
        /*commentDetails.forEach(d -> {
            System.out.println(d.getNickName() + ">>>>" + d.getContent());
        });*/
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
                List<seo.market.vo.CommentDetail> commentDetails = obj.getCommentDetails();
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
        public String call() throws Exception {
            return processFile(new File(filePath));
        }
    }
}