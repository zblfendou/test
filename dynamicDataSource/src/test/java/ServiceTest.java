import com.dynamicDataSource.models.Clazz;
import com.dynamicDataSource.models.School;
import com.dynamicDataSource.services.ClazzService;
import com.dynamicDataSource.services.SchoolService;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;

import javax.inject.Inject;

/**
 * Created by zbl on 2017/8/29.
 */
public class ServiceTest extends AbstractTest {
    @Inject
    private SchoolService schoolService;
    @Inject
    private ClazzService clazzService;

    @Test
    public void add() {
        School school = new School();
        school.setSchoolName("test schoolName");
        school.setSchoolAddress("test schoolAddress");
        School s = schoolService.add(school);
        outputJson(s);
    }

    @Test
    public void addClazz() {
        Clazz clazz = new Clazz();
        clazz.setClazzName("test ClazzName");
        clazzService.add(clazz);
    }
}
