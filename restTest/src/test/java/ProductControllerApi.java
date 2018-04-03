import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ProductControllerApi {
    private static ObjectMapper objectMapper = null;

    static {
        objectMapper = new ObjectMapper();
    }
@Test
    public void testSave() {
        //设置header
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json; charset=UTF-8");

        //设置参数
        Map<String, String> hashMap = new LinkedHashMap<String, String>();
        hashMap.put("random", "1234556");
        hashMap.put("orderNo", "Z20170327110912921426");
        hashMap.put("requestSource","");
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<Map<String, String>>(hashMap, httpHeaders);

        //执行请求
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> resp = restTemplate.exchange("http://localhost:8081/msp/saveSaasFrequencySaleInfo.do",HttpMethod.POST,requestEntity, String.class);

        //获取返回的header
        List<String> val = resp.getHeaders().get("Set-Cookie");
        System.out.println(val);

        //获得返回值
        String body = resp.getBody();
        System.out.println(body);
    }
}
