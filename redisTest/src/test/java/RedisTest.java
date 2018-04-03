import com.redis.SpringBootApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringBootApplication.class)
@WebAppConfiguration
@Transactional
public class RedisTest {

    @Test
    public void add() {
        String url = "http://localhost:8082/add.do";
        sendUrlAndReturn(url);
    }

    @Test
    public void getAll() {
        String url = "http://localhost:8082/getAll.do";
        sendUrlAndReturn(url);
    }

    @Test
    public void getKeys() {
        String url = "http://localhost:8082/getKeys.do";
        sendUrlAndReturn(url);
    }

    @Test
    public void getKey() {
        String url = "http://localhost:8082/get.do?redisKey=zhangsanKey01";
        sendUrlAndReturn(url);
    }

    @Test
    public void remove() {
        String url = "http://localhost:8082/remove.do?redisKey=zhangsanKey01";
        sendUrlAndReturn(url);
    }

    @Test
    public void empty() {
        String url = "http://localhost:8082/empty.do";
        sendUrlAndReturn(url);
    }

    @Test
    public void count() {
        String url = "http://localhost:8082/count.do";
        sendUrlAndReturn(url);
    }

    @Test
    public void isKeyExists() {
        String url = "http://localhost:8082/isKeyExists.do?redisKey=zhangsanKey01";
        sendUrlAndReturn(url);
    }

    @Test
    public void saveRedis() {
        String url = "http://localhost:8082/save.do?redisKey=zhangsanKey01&name=zbl&tel=1234&address=北京1";
        sendUrlAndReturn(url);
    }
    @Test
    public void getRedis() {
        String url = "http://localhost:8082/get.do?redisKey=zhangsanKey01";
        sendUrlAndReturn(url);
    }


    private void sendUrlAndReturn(String url) {
        HttpHeaders httpHeaders = getHttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        String body = exchange.getBody();
        System.out.println(body);
    }


    private HttpHeaders getHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json; charset=UTF-8");
        return httpHeaders;
    }
}
