package schoolService;

import com.dubbotest.client.models.Clazz;
import com.dubbotest.service.models.School;
import com.dubbotest.service.services.SchoolService;
import org.junit.Test;

import javax.inject.Inject;

/**
 * Created by zbl on 2017/8/28.
 */
public class SchoolServiceTest extends AbstractTest {
    @Inject
    private SchoolService schoolService;


    @Test
    public void addSchool() {
        School school = new School();
        school.setSchoolName("test SchoolName");
        school.setSchoolAddress("test SchoolAddress");
        School s = schoolService.addSchool(school);
        outputJson(s);
    }

    @Test
    public void addClazz() {
        Clazz clazz = new Clazz();
        clazz.setClazzName("test clazzName");
        Clazz c = schoolService.addClazz(clazz);
        outputJson(c);
    }
}
