import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import vo.SiteOrderReq;
import vo.SiteSpec;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class SiteApiTest extends AbstractTest {
    private static final Logger logger = LoggerFactory.getLogger(SiteApiTest.class);
    private static final String salt = "QnxiMPS6cR7OZOOj2i7hht5v7NqToNB94IdLM1VLnp3GO5UF9iuvWgfgeCSBv6SwpoDC09GnoSfvdgBvjooXX0nv2NdQXeQCsxeyk5i3/lZ0ddGYf3b82WVp+mlYW7Q5";
    public static final String PRODUCT_CODE = "1";
    public static final String AGENT_CODE = "29";
    public static final String SERVICE_CODE = UUID.randomUUID().toString();
    private static ObjectMapper objectMapper = null;

    static {
        objectMapper = new ObjectMapper();
    }

    /**
     * 创建网站
     */
    @Test
    public void createSite() throws JsonProcessingException, ParseException {
        SiteOrderReq siteOrderReq = createSiteReq();
        HttpHeaders httpHeaders = getHttpHeaders(siteOrderReq);
        String param = objectMapper.writeValueAsString(siteOrderReq);
        HttpEntity<String> entity = new HttpEntity<>(param, httpHeaders);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange("http://localhost:8090/siteApi/createSite.do", HttpMethod.POST, entity, String.class);
        String responseEntityBody = responseEntity.getBody();
        System.out.println(responseEntityBody);
    }

    /**
     * 获取价格
     * @throws JsonProcessingException
     */
    @Test
    public void getPrice() throws JsonProcessingException {
        SiteOrderReq dreamSiteReq = createDreamSiteReq();
        HttpHeaders httpHeaders = getHttpHeaders(dreamSiteReq);
        String param = objectMapper.writeValueAsString(dreamSiteReq);
        HttpEntity<String> entity = new HttpEntity<>(param, httpHeaders);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange("http://localhost:8090/siteApi/getPrice.do", HttpMethod.POST, entity, String.class);
        String responseEntityBody = responseEntity.getBody();
        System.out.println(responseEntityBody);
    }

    /**
     * 获取网站信息
     */
    @Test
    public void getSiteInfo() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json; charset=UTF-8");
        String req_time = String.valueOf(System.currentTimeMillis());
        httpHeaders.add("REQ_TIME", req_time);//请求时间
        String req_id = UUID.randomUUID().toString();
        httpHeaders.add("REQ_ID", req_id);//请求ID
        String client_id = "XINNET";
        httpHeaders.add("CLIENT_ID", client_id);//调用方标识

        String str = "agentCode" + AGENT_CODE + req_time + req_id + salt;
        MessageDigest digest = MessageDigest.getInstance("MD5");
        byte[] rs = digest.digest(str.getBytes("UTF-8"));
        StringBuilder digestHexStr = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            digestHexStr.append(byteHEX(rs[i]));
        }
        httpHeaders.add("SIGN", digestHexStr.toString());//数据签名
        HttpEntity<Object> entity = new HttpEntity<>(httpHeaders);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange("http://localhost:8090/siteApi/getSiteInfo.do?agentCode="+AGENT_CODE, HttpMethod.GET, entity, String.class);
        String responseEntityBody = responseEntity.getBody();
        System.out.println(responseEntityBody);
    }

    /**
     * 退费
     */
    @Test
    public void drawback() throws JsonProcessingException, ParseException {
        createSite();
        SiteOrderReq drawbackSiteOrderReq = createDrawbackSiteOrderReq();
        HttpHeaders httpHeaders = getHttpHeaders(drawbackSiteOrderReq);
        String param = objectMapper.writeValueAsString(drawbackSiteOrderReq);
        HttpEntity<String> entity = new HttpEntity<>(param, httpHeaders);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange("http://localhost:8090/siteApi/drawback.do", HttpMethod.POST, entity, String.class);
        String responseEntityBody = responseEntity.getBody();
        System.out.println(responseEntityBody);
    }

    private SiteOrderReq createDrawbackSiteOrderReq() {
        SiteOrderReq siteOrderReq = new SiteOrderReq();
        siteOrderReq.setServiceCode(SERVICE_CODE);
        siteOrderReq.setProductCode(PRODUCT_CODE);
        return siteOrderReq;
    }

    /**
     * 设置headers
     */
    private HttpHeaders getHttpHeaders(SiteOrderReq siteOrderReq) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json; charset=UTF-8");
        String req_time = String.valueOf(System.currentTimeMillis());
        httpHeaders.add("REQ_TIME", req_time);//请求时间
        String req_id = UUID.randomUUID().toString();
        httpHeaders.add("REQ_ID", req_id);//请求ID
        String client_id = "XINNET";
        httpHeaders.add("CLIENT_ID", client_id);//调用方标识
        String sign = createSiteSign(siteOrderReq, req_time, req_id);
        httpHeaders.add("SIGN", sign);//数据签名
        return httpHeaders;
    }

    /**
     * 创建超云网站
     */
    private SiteOrderReq createDreamSiteReq() {
        SiteOrderReq siteOrderReq = new SiteOrderReq();
        siteOrderReq.setTimeAmount(2);
        siteOrderReq.setProductCode("2");
        SiteSpec siteSpec = new SiteSpec();
        siteSpec.setFunctions("high");
        siteSpec.setService("middle");
        siteSpec.setLanguage("both");
        siteSpec.setTerminal("mobile");
        siteOrderReq.setSpec(siteSpec);
        return siteOrderReq;
    }

    /**
     * 创建云定制
     */
    private SiteOrderReq createSiteReq() {
        SiteOrderReq siteOrderReq = new SiteOrderReq();
        siteOrderReq.setAgentCode(AGENT_CODE);
        siteOrderReq.setFreeTimeAmount(1);
        siteOrderReq.setProductCode(PRODUCT_CODE);
        siteOrderReq.setServiceCode(SERVICE_CODE);
        siteOrderReq.setTimeAmount(3);
        return siteOrderReq;
    }

    /**
     * 创建签名数据
     */
    private String createSiteSign(SiteOrderReq siteOrderReq, String req_time, String req_id) {
        try {
            String objJson = objectMapper.writeValueAsString(siteOrderReq);
            String str = objJson + req_time + req_id + salt;
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] rs = digest.digest(str.getBytes("UTF-8"));
            StringBuilder digestHexStr = new StringBuilder();
            for (int i = 0; i < 16; i++) {
                digestHexStr.append(byteHEX(rs[i]));
            }
            return digestHexStr.toString();
        } catch (JsonProcessingException | NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取十六进制串
     */
    public static String byteHEX(byte ib) {
        char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] ob = new char[2];
        ob[0] = Digit[(ib >>> 4) & 0X0F];
        ob[1] = Digit[ib & 0X0F];
        return new String(ob);
    }
}
