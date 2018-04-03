import com.fasterxml.jackson.databind.ObjectMapper;
import com.aop.SpringBootApplication;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

/**
 * Created by zbl on 2017/8/22.
 */
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringBootApplication.class)
@WebAppConfiguration
@Transactional
public class AbstractTest {
    protected void outputJson(Object data) {
        try {
            System.out.println("JSON:");
            System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(data));
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
