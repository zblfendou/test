package market.seo;

import lombok.extern.slf4j.Slf4j;
import market.seo.service.DataService;
import market.seo.utils.DataUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private String scanFilePath = "D:\\marketService\\zl\\seo文件\\1\\";

    @RequestMapping("/test")
    public String test() throws InterruptedException {
        long before = System.currentTimeMillis();
        dataService.deleteAll();
        int fileCount = dataUtil.buildDataAndSave(scanFilePath);
        long after = System.currentTimeMillis();
        return String.format("数据量为:%d条,共计耗时:%d", fileCount, (after - before) / 1000);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        log.debug("started ! ! !");
    }
}
