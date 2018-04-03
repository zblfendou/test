import com.dynamicDataSource1.primary.models.User;
import com.dynamicDataSource1.primary.services.UserService;
import com.dynamicDataSource1.second.models.Student;
import com.dynamicDataSource1.second.services.StudentService;
import org.junit.Test;

import javax.inject.Inject;

public class ServiceTest extends AbstractTest {
    @Inject
    private UserService userService;

    @Inject
    private StudentService studentService;

    @Test
    public void addUser() {
        User user = new User();
        user.setUserName("test username");
        user.setPassword("test password");
        User u = userService.add(user);
        outputJson(u);
    }
    @Test
    public void addStudent() {
        Student student = new Student();
        student.setStudentName("test studentname");
        student.setStudentPassword("test studentpassword");
        Student s = studentService.add(student);
        outputJson(s);
    }
}
