package market.seo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import market.seo.models.APP;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public interface AppService {
    void save(List<APP> apps);

    void test();

}
