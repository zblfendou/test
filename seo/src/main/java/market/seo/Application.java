package market.seo;

import lombok.extern.slf4j.Slf4j;
import market.seo.service.AppService;
import market.seo.service.DataService;
import market.seo.utils.APPAnswerListUtil;
import market.seo.utils.APPAnswerUtil;
import market.seo.utils.APPUtil;
import market.seo.utils.DataUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;

@SpringBootApplication
@EntityScan("market.seo.**.models")
@EnableJpaRepositories("market.seo.**.daos")
@EnableTransactionManagement
@RestController
@Slf4j
public class Application {
    @Inject
    private DataUtil dataUtil;
    @Inject
    private DataService dataService;
    private String scanDataFilePath = "D:\\marketService\\zl\\seo文件\\1\\";
    @Inject
    private AppService appService;
    @Inject
    private APPUtil appUtil;
    private String scanAPPFilePath = "D:\\marketService\\zl\\seo文件\\app\\详情页1-48000\\";
    @Inject
    private APPAnswerUtil appAnswerUtil;
    private String scanAPPAnswerFilePath = "D:\\marketService\\zl\\seo文件\\APP问答\\APP问答\\";
    @Inject
    private APPAnswerListUtil appAnswerListUtil;
    private String scanAPPAnswerListFilePath = "D:\\marketService\\zl\\seo文件\\APP问答\\列表\\";


    /**
     * seo问答
     */
    @RequestMapping("/test")
    public String test() throws InterruptedException {
        long before = System.currentTimeMillis();
        dataService.deleteAll();
        int fileCount = dataUtil.buildDataAndSave(scanDataFilePath);
        long after = System.currentTimeMillis();
        return String.format("数据量为:%d条,共计耗时:%d", fileCount, (after - before) / 1000);
    }

    /**
     * seo app 评论
     */
    @RequestMapping("/appcomment")
    public String appcomment() throws InterruptedException {
        long before = System.currentTimeMillis();
        appService.test();
        long after = System.currentTimeMillis();
        return String.format("共计耗时:%d", (after - before) / 1000);
    }

    /**
     * seo app 应用
     */
    @RequestMapping("/saveApps")
    public String saveApps() throws InterruptedException {
        long before = System.currentTimeMillis();
        int fileCount = appUtil.buildAPPAndSave(scanAPPFilePath);
        long after = System.currentTimeMillis();
        return String.format("数据量为:%d条,共计耗时:%d", fileCount, (after - before) / 1000);
    }

    /**
     * seo app 应用
     */
    @RequestMapping("/saveAppAnswer")
    public String saveAppAnswers() throws InterruptedException {
        long before = System.currentTimeMillis();
        int fileCount = appAnswerUtil.buildAPPAnswerAndSave(scanAPPAnswerFilePath);
        long after = System.currentTimeMillis();
        return String.format("数据量为:%d条,共计耗时:%d", fileCount, (after - before) / 1000);
    }

    /**
     * seo app 问答 list
     */
    @RequestMapping("/saveAppAnswerList")
    public String saveAppAnswerLists() throws InterruptedException {
        long before = System.currentTimeMillis();
        int fileCount = appAnswerListUtil.buildAPPAnswerAndSave(scanAPPAnswerListFilePath);
        long after = System.currentTimeMillis();
        return String.format("数据量为:%d条,共计耗时:%d", fileCount, (after - before) / 1000);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        log.debug("started ! ! !");
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
