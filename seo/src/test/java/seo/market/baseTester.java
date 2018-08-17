package seo.market;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ServiceTesterConfig.class,webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
public class baseTester {
    @Inject
    ObjectMapper objectMapper;

    protected void outputJson(Object data) {
        try {
            System.out.println("JSON:");
            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(data));
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
