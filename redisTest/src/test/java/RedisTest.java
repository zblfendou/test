import com.redis.SpringBootApplication;
import com.redis.models.RedisModel;
import com.redis.service.RedisModelService;
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

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringBootApplication.class)
@WebAppConfiguration
@Transactional
public class RedisTest {
    @Inject
    private RedisModelService service;

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
        String url = "http://localhost:8082/save.do?redisKey=zhangsanKey01&name=zbl&tel=1234&address=北京4";
        sendUrlAndReturn(url);
    }

    @Test
    public void getRedis() {
        String url = "http://localhost:8082/get.do?redisKey=zhangsanKey01";
        sendUrlAndReturn(url);
    }

    @Test
    public void delete() {
        String url = "http://localhost:8082/delete.do?redisKey=zhangsanKey01";
        sendUrlAndReturn(url);
    }

    @Test
    public void manyThreadTest() {//测试缓存中aync属性 多线程访问时,如果aync设置true 则从数据库取一次数据 其他线程等待数据返回 ,然后取返回数据 .如果false则分别访问数据库
        int number = 3;//线程数
        ExecutorService threadPool = Executors.newFixedThreadPool(number);
        List<Future<RedisModel>> results = new ArrayList<>();
        String redisKey = "zhangsanKey01";
        for (int i = 0; i < number; i++) {
            Future<RedisModel> future = threadPool.submit(new MyThread(redisKey));
            results.add(future);
        }
        for (Future<RedisModel> r : results) {
            try {
                System.out.println(r.get().getRedisKey());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
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

    private class MyThread implements Callable<RedisModel> {
        private String redisKey;

        public MyThread(String redisKey) {
            this.redisKey = redisKey;
        }

        @Override
        public RedisModel call() throws Exception {
            return service.get(redisKey);
        }
    }
}
