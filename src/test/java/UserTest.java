import com.yykj.oachat.service.IUserInfoService;
import com.yykj.oachat.util.GsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Lee
 * @date 2017/12/5
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = "classpath:spring/spring-service.xml")
public class UserTest {

    @Autowired
    private IUserInfoService userInfoService;

    @Test
    public void testGetAllInformation(){
        System.out.println(GsonUtil.getInstance().toJson(userInfoService.getAllInformation(1L)));
    }
}
