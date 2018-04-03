import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import vo.SiteOrderReq;

public class HttpClientTest {

    private static final String salt = "QnxiMPS6cR7OZOOj2i7hht5v7NqToNB94IdLM1VLnp3GO5UF9iuvWgfgeCSBv6SwpoDC09GnoSfvdgBvjooXX0nv2NdQXeQCsxeyk5i3/lZ0ddGYf3b82WVp+mlYW7Q5";
    public static final String PRODUCT_CODE = "1";
    public static final String AGENT_CODE = "29";
    public static final String SERVICE_CODE = UUID.randomUUID().toString();
    private static ObjectMapper objectMapper = null;

    static{
        objectMapper = new ObjectMapper();
    }

    @SuppressWarnings("deprecation")
    public static void main(String[] args) throws UnsupportedEncodingException, JsonProcessingException {
//        HttpClient httpclient = new DefaultHttpClient();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        /* initialize the request method */
        // prepare the request url
        HttpPost httpPost = new HttpPost("http://localhost:8090/siteApi/createSite.do");
        // prepare the request parameters
        SiteOrderReq siteOrderReq = createSiteReq();
        String param = objectMapper.writeValueAsString(siteOrderReq);
        StringEntity entity = new StringEntity(param,"UTF-8");
        setHeaders(httpPost, siteOrderReq);
        // set the request entity
        httpPost.setEntity(entity);

        /* execute the request */
        System.out.println("executing request " + httpPost.getURI());
        HttpResponse response = null;
        try {
            response = httpclient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /* check whether it has relocated */
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == HttpStatus.SC_MOVED_TEMPORARILY ||
                statusCode == HttpStatus.SC_MOVED_PERMANENTLY ||
                statusCode == HttpStatus.SC_SEE_OTHER ||
                statusCode == HttpStatus.SC_TEMPORARY_REDIRECT) {

            Header[] headers = response.getHeaders("location");

            if (headers != null) {
                httpPost.releaseConnection();
                String newUrl = headers[0].getValue();
                httpPost.setURI(URI.create(newUrl));
                try {
                    response = httpclient.execute(httpPost);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        /* print the result of the request */
        try {
            Utils.printResponse(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Do not feel like reading the response body
        // Call abort on the request object
        httpPost.abort();
        // When HttpClient instance is no longer needed,
        // shut down the connection manager to ensure
        // immediate deallocation of all system resources
        httpclient.getConnectionManager().shutdown();
    }

    private static void setHeaders(HttpPost httpPost, SiteOrderReq siteOrderReq) {
        httpPost.setHeader("Content-Type", "application/json; charset=UTF-8");
        String req_time = String.valueOf(System.currentTimeMillis());
        httpPost.setHeader("REQ_TIME", req_time);
        String req_id = UUID.randomUUID().toString();
        httpPost.setHeader("REQ_ID", req_id);//请求ID
        String client_id = "XINNET";
        httpPost.setHeader("CLIENT_ID", client_id);//调用方标识
        String sign = createSiteSign(siteOrderReq, req_time, req_id);
        httpPost.setHeader("SIGN", sign);//数据签名
    }

    private static String createSiteSign(SiteOrderReq siteOrderReq, String req_time, String req_id) {
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

    /**
     * 创建云定制
     */
    private static SiteOrderReq createSiteReq() {
        SiteOrderReq siteOrderReq = new SiteOrderReq();
        siteOrderReq.setAgentCode(AGENT_CODE);
        siteOrderReq.setFreeTimeAmount(1);
        siteOrderReq.setProductCode(PRODUCT_CODE);
        siteOrderReq.setServiceCode(SERVICE_CODE);
        siteOrderReq.setTimeAmount(3);
        return siteOrderReq;
    }
}
