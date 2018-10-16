package seo.market;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import market.seo.models.SeoAnswer;
import market.seo.service.DataService;
import market.seo.utils.DataUtil;
import market.seo.vo.SeoDataVO;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class test extends baseTester {
    @Inject
    private DataService dataService;
    @Inject
    private DataUtil dataUtil;

    private String scanFilePath1 = "D:\\marketService\\zl\\seo文件\\1\\";
    private String scanFilePath2 = "D:\\marketService\\zl\\seo文件\\2\\";
    private String scanFilePath3 = "D:\\marketService\\zl\\seo文件\\3\\";
    private String scanFilePath4 = "D:\\marketService\\zl\\seo文件\\4\\";
    private String scanFilePath5 = "D:\\marketService\\zl\\seo文件\\5\\";

    @Test
    public void insertData() {
        /*String[] strings = { scanFilePath1*//*,scanFilePath2/*, scanFilePath3,scanFilePath4,  scanFilePath5*//*};
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        CountDownLatch countDownLatch = new CountDownLatch(strings.length);
        Arrays.stream(strings).forEach(path -> executorService.submit(() -> {
            try {
                System.out.println(Thread.currentThread().getName());
                dataUtil.buildDataAndSave(path);
                countDownLatch.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }));
        try {
            countDownLatch.await();
            executorService.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        dataService.washDataAndSaveSeoAnswers();
    }

    @Test
    public void getSeoAnswer() {
        List<SeoAnswer> allSeoAnswer = dataService.getAllSeoAnswer();
        allSeoAnswer.forEach(seoAnswer -> {
            TypeReference<List<SeoDataVO>> typeReference = new TypeReference<List<SeoDataVO>>() {
            };
            try {
                List<SeoDataVO> seoDataVOList = objectMapper.readValue(seoAnswer.getData(), typeReference);
                outputJson(seoAnswer);
                outputJson(seoDataVOList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
