import com.aop.models.User;
import com.aop.services.UserService;
import org.junit.Test;

import javax.inject.Inject;

/**
 * Created by zbl on 2017/8/23.
 */
public class UserServiceTest extends AbstractTest {
    @Inject
    private UserService userService;

    @Test
    public void add() {
        User user = new User();
        user.setName("test name");
        user.setPwd("test pwd");
        User u = userService.add(user);
        System.out.println(u.getName()+"  "+u.getPwd()+" "+u.getId());
    }
}
