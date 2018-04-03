import org.junit.Test;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RestTemplateDemoTest extends AbstractTest{

    @Test
    public void simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(1000);
        requestFactory.setReadTimeout(1000);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        Map map=restTemplate.getForObject("https://api.weixin.qq.com/cgi-bin/getcallbackip?access_token=ACCESS_TOKEN",Map.class);
        System.out.println(map);
        System.out.println(map.get("errcode"));
        System.out.println(map.get("errmsg"));
    }

    /**
     * 发送一个get请求，并接受封装成map
     */
    @Test
    public void restTemplateMap() {
        RestTemplate restTemplate = new RestTemplate();
        String map=restTemplate.getForObject("http://172.23.170.52:8080/test.do",String.class);
        System.out.println(map);
    }

    /**
     * 发送一个get请求，并接受封装成string
     */
    @Test
    public void restTemplateString() {
        RestTemplate restTemplate = new RestTemplate();
        String str=restTemplate.getForObject("https://api.weixin.qq.com/cgi-bin/getcallbackip?access_token=ACCESS_TOKEN",String.class);
        System.out.println(str);
    }


    /**
     * 添加消息头
     */
    @Test
    public void httpHeaders() {
        final String uri = "https://api.weixin.qq.com/cgi-bin/getcallbackip?access_token=ACCESS_TOKEN";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
        System.out.println(result);
    }


    /**
     * 添加请求参数以及消息头
     */
    @Test
    public void getPolicyJson(){
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
        ResponseEntity<String> resp = restTemplate.exchange("https://api.weixin.qq.com/cgi-bin/getcallbackip?access_token=ACCESS_TOKEN",HttpMethod.POST,requestEntity, String.class);

        //获取返回的header
        List<String> val = resp.getHeaders().get("Set-Cookie");
        System.out.println(val);

        //获得返回值
        String body = resp.getBody();
        System.out.println(body);
    }

}