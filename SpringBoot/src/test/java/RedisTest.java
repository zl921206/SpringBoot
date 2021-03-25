import com.alibaba.fastjson.JSONObject;
import com.demo.entity.User;
import com.demo.service.RedisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = com.demo.SpringBootApp.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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
        redisService.setCacheObject("user:" + userId, user, 30L, TimeUnit.SECONDS);
        System.out.println("存入时间：" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
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
