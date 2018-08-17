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
import java.util.List;

@Slf4j
public class test extends baseTester {
    @Inject
    private DataService dataService;
    @Inject
    private DataUtil dataUtil;

    private String scanFilePath = "D:\\marketService\\zl\\seo文件\\1\\";

    @Test
    public void insertData() throws InterruptedException {
        long before = System.currentTimeMillis();
        dataService.deleteAll();
        int fileCount = dataUtil.buildDataAndSave(scanFilePath);
        long after = System.currentTimeMillis();
        System.out.println(String.format("插入数据耗时:%d 秒", ((after - before) / 1000)));
    }


    @Test
    public void washDataAndSaveSeoAnswers() {
        dataService.washDataAndSaveSeoAnswers();
    }

    @Test@Rollback(false)
    public void getSeoAnswer() {
        washDataAndSaveSeoAnswers();
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
