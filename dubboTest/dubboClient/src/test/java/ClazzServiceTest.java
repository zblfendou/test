import com.dubbotest.client.models.Clazz;
import com.dubbotest.client.service.ClazzService;
import org.junit.Test;

import javax.inject.Inject;

/**
 * Created by zbl on 2017/8/28.
 */
public class ClazzServiceTest extends AbstractTest {
    @Inject
    private ClazzService clazzService;

    @Test
    public void add() {
        Clazz clazz = new Clazz();
        clazz.setClazzName("test clazzName");
        Clazz c = clazzService.add(clazz);
        outputJson(c);
    }
}
