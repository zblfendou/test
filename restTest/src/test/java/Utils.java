import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
public class Utils {
    public static void printResponse(HttpResponse response)throws Exception{  
        /* print the result of the request */
        HttpEntity entity = response.getEntity();
        System.out.println("--------------------");
        // print the status line   
        System.out.println(response.getStatusLine());
        // print the headers  
        System.out.println("========== headers ==========");
        Header[] headers=response.getAllHeaders();
        for(Header header:headers){
            System.out.println(header);
        }
        // print the content length  
        if (entity != null) {
            System.out.println("\nResponse content length: "+
                    entity.getContentLength());
        }
        // print the entity content  
        System.out.println("\n========== response content ==========");
        BufferedReader is=new BufferedReader(
                new InputStreamReader(entity.getContent()));
        String line=null;
        while((line=is.readLine())!=null){
            System.out.println(line);
        }
        System.out.println("--------------------");
    }
}  