import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.hasText;

public class RestTemplateDemoTest extends AbstractTest {
    @Inject
    private ObjectMapper objectMapper;

    @Test
    public void simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(1000);
        requestFactory.setReadTimeout(1000);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        Map map = restTemplate.getForObject("https://api.weixin.qq.com/cgi-bin/getcallbackip?access_token=ACCESS_TOKEN", Map.class);
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
        String map = restTemplate.getForObject("http://172.23.170.52:8080/test.do", String.class);
        System.out.println(map);
    }

    /**
     * 发送一个get请求，并接受封装成string
     */
    @Test
    public void restTemplateString() {
        RestTemplate restTemplate = new RestTemplate();
        String str = restTemplate.getForObject("https://api.weixin.qq.com/cgi-bin/getcallbackip?access_token=ACCESS_TOKEN", String.class);
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
    public void getPolicyJson() {
        //设置header
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json; charset=UTF-8");

        //设置参数
        Map<String, String> hashMap = new LinkedHashMap<String, String>();
        hashMap.put("random", "1234556");
        hashMap.put("orderNo", "Z20170327110912921426");
        hashMap.put("requestSource", "");
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<Map<String, String>>(hashMap, httpHeaders);

        //执行请求
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> resp = restTemplate.exchange("https://api.weixin.qq.com/cgi-bin/getcallbackip?access_token=ACCESS_TOKEN", HttpMethod.POST, requestEntity, String.class);

        //获取返回的header
        List<String> val = resp.getHeaders().get("Set-Cookie");
        System.out.println(val);

        //获得返回值
        System.out.println(resp.getBody());
    }

    @Test
    public void yimiao_api() {
        //设置header
        HttpHeaders httpHeaders = new HttpHeaders();
        //设置参数
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(map, httpHeaders);

        //执行请求
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> resp = restTemplate.exchange("https://m.baidu.com/sf?openapi=1&dspName=iphone&from_sf=1&pd=disease_kgb&resource_id=5197&word=%E7%96%AB%E8%8B%97%E6%9F%A5%E8%AF%A2&group=detail&id=139&lid=7643041603108264261", HttpMethod.GET, requestEntity, String.class);

        //获得返回值
        Object body = resp.getBody();
        try {
            System.out.println(String.format("返回结果:%s", objectMapper.writeValueAsString(body)));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void distributorAPI_detail() {
        //设置header
        HttpHeaders httpHeaders = new HttpHeaders();
        String timestamp = String.valueOf(System.currentTimeMillis());
        String partnerCode = "hy5192032";
        String nonce = UUID.randomUUID().toString();
        httpHeaders.add("X_API_TIMESTAMP", timestamp);
        httpHeaders.add("X_API_NONCE", nonce);
        httpHeaders.add("X-API-PARTNERCODE", partnerCode);
        System.out.println(String.format("X_API_TIMESTAMP:%s", timestamp));
        System.out.println(String.format("X_API_NONCE:%s", nonce));
        System.out.println(String.format("X-API-PARTNERCODE:%s", partnerCode));
        String salt = "hV5NJAhea38t1yJ8iXV4CaJcvPPxTEfjQTmd0z7cySHyIWtL5zMKJGfRZBRH0kL8anzx1QxJjhEArhDWXXAyg2izqDQgJyKSMDKnpiFFP3uoROPAMm6T6F7tWEvfFWrt";
        Map<String, String[]> requestParamters = new HashMap<>();
        requestParamters.put("productCode", new String[]{"saas2018050921432"});
        String sign = sign(timestamp, nonce, partnerCode, requestParamters, salt);
        httpHeaders.add("X_API_SIGNATURE", sign);
        System.out.println(String.format("X_API_SIGNATURE:%s", sign));
        //设置参数
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
//        map.add("productCode", "saas2018050921432");
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(map, httpHeaders);

        //执行请求
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Object> resp = restTemplate.exchange("http://market.xinnet.com/api/distributor/getProductDetails?productCode=saas2018050921432", HttpMethod.GET, requestEntity, Object.class);

        //获得返回值
        Object body = resp.getBody();
        try {
            System.out.println(String.format("返回结果:%s", objectMapper.writeValueAsString(body)));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void distributorAPI_createOrder() {
        //设置header
        HttpHeaders httpHeaders = new HttpHeaders();
        String timestamp = String.valueOf(System.currentTimeMillis());
        String partnerCode = "hy5192032";
        String nonce = UUID.randomUUID().toString();
        httpHeaders.add("X_API_TIMESTAMP", timestamp);
        httpHeaders.add("X_API_NONCE", nonce);
        httpHeaders.add("X-API-PARTNERCODE", partnerCode);
        System.out.println(String.format("X_API_TIMESTAMP:%s", timestamp));
        System.out.println(String.format("X_API_NONCE:%s", nonce));
        System.out.println(String.format("X-API-PARTNERCODE:%s", partnerCode));
        String salt = "hV5NJAhea38t1yJ8iXV4CaJcvPPxTEfjQTmd0z7cySHyIWtL5zMKJGfRZBRH0kL8anzx1QxJjhEArhDWXXAyg2izqDQgJyKSMDKnpiFFP3uoROPAMm6T6F7tWEvfFWrt";
        Map<String, String[]> requestParamters = new HashMap<>();
        requestParamters.put("clientCode", new String[]{"hy5192032"});
        requestParamters.put("productID", new String[]{"538"});
        requestParamters.put("infoVersion", new String[]{"8"});
        requestParamters.put("specID", new String[]{"76"});
        requestParamters.put("timeLength", new String[]{"3"});
        requestParamters.put("timeUnit", new String[]{"MONTH"});
        requestParamters.put("price", new String[]{"35"});
        requestParamters.put("extItems", new String[]{"{\"q\":\"3\",\"w\":\"枚举1\"}"});
        String sign = sign(timestamp, nonce, partnerCode, requestParamters, salt);
        httpHeaders.add("X_API_SIGNATURE", sign);
        System.out.println(String.format("X_API_SIGNATURE:%s", sign));
        //设置参数
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        map.add("clientCode", "hy5192032");
        map.add("productID", "538");
        map.add("infoVersion", "8");
        map.add("specID", "76");
        map.add("price", "35");
        map.add("timeLength", "3");
        map.add("timeUnit", "MONTH");
        map.add("extItems", "{\"q\":\"3\",\"w\":\"枚举1\"}");
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(map, httpHeaders);
        //执行请求
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Object> resp = restTemplate.exchange("http://market.xinnet.com/api/distributor/createOrder", HttpMethod.POST, requestEntity, Object.class);

        //获得返回值
        Object body = resp.getBody();
        try {
            System.out.println(String.format("返回结果:%s", objectMapper.writeValueAsString(body)));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void distributorAPI_calculateCreateOrderPrices() {
        //设置header
        HttpHeaders httpHeaders = new HttpHeaders();
        String timestamp = String.valueOf(System.currentTimeMillis());
        String partnerCode = "hy5192032";
        String nonce = UUID.randomUUID().toString();
        httpHeaders.add("X_API_TIMESTAMP", timestamp);
        httpHeaders.add("X_API_NONCE", nonce);
        httpHeaders.add("X-API-PARTNERCODE", partnerCode);
        System.out.println(String.format("X_API_TIMESTAMP:%s", timestamp));
        System.out.println(String.format("X_API_NONCE:%s", nonce));
        System.out.println(String.format("X-API-PARTNERCODE:%s", partnerCode));
        String salt = "hV5NJAhea38t1yJ8iXV4CaJcvPPxTEfjQTmd0z7cySHyIWtL5zMKJGfRZBRH0kL8anzx1QxJjhEArhDWXXAyg2izqDQgJyKSMDKnpiFFP3uoROPAMm6T6F7tWEvfFWrt";
        Map<String, String[]> requestParamters = new HashMap<>();
        requestParamters.put("productID", new String[]{"538"});
        requestParamters.put("infoVersion", new String[]{"8"});
        requestParamters.put("specID", new String[]{"76"});
        requestParamters.put("timeLength", new String[]{"3"});
        requestParamters.put("timeUnit", new String[]{"MONTH"});
        requestParamters.put("extItems", new String[]{"{\"q\":\"3\",\"w\":\"枚举1\"}"});
        String sign = sign(timestamp, nonce, partnerCode, requestParamters, salt);
        httpHeaders.add("X_API_SIGNATURE", sign);
        System.out.println(String.format("X_API_SIGNATURE:%s", sign));
        //设置参数
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        map.add("productID", "538");
        map.add("infoVersion", "8");
        map.add("specID", "76");
        map.add("timeLength", "3");
        map.add("timeUnit", "MONTH");
        map.add("extItems", "{\"q\":\"3\",\"w\":\"枚举1\"}");
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(map, httpHeaders);
        //执行请求
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Object> resp = restTemplate.exchange("http://market.xinnet.com/api/distributor/calculateCreateOrderPrices", HttpMethod.POST, requestEntity, Object.class);

        //获得返回值
        Object body = resp.getBody();
        try {
            System.out.println(String.format("返回结果:%s", objectMapper.writeValueAsString(body)));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }


    private String sign(final String timestamp, final String nonce, final String partnerCode, final Map<String, String[]> requestParameters, String salt) {
        String collect = requestParameters.entrySet()
                .stream()
                .map(entry -> {
                    String key = entry.getKey();
                    String value = Arrays.stream(entry.getValue())
                            .map(this::encode)
                            .filter(StringUtils::hasText)
                            .sorted()
                            .collect(Collectors.joining(","));
                    if (hasText(value)) return key + '=' + value;
                    else return null;
                })
                .filter(StringUtils::hasText)
                .sorted()
                .collect(Collectors.joining("&"));
        System.out.println("排序后的参数:"+collect);
        String toSign = timestamp
                + nonce
                + partnerCode
                + collect
                + salt;
        System.out.println(String.format("toSign:%s", toSign));
        return DigestUtils.md5Hex(toSign);
    }

    private String encode(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

}