import com.alibaba.fastjson.JSONObject;
import com.demo.entity.User;
import com.demo.service.RedisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = com.demo.SpringBootApp.class)
public class RedisTest {

    @Resource
    RedisService redisService;

    /**
     * 存入对象
     */
    @Test
    public void addRedis(){
        Long userId = 100000L;
        User user = new User();
        user.setUserId(userId);
        user.setUserName("zhangsan");
        user.setToken("ABCDEFG");
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(new Date());
        redisService.setCacheObject("user:" + userId, user);
    }

    /**
     * 获取对象
     */
    @Test
    public void getRedis(){
        Long userId = 100000L;
        Object cacheObject = redisService.getCacheObject("user:" + userId);
        User user = (User)cacheObject;
        System.out.println("user: " + JSONObject.toJSONString(user));
    }

}
