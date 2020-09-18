import com.alibaba.fastjson.JSONObject;
import com.demo.entity.User;
import com.demo.service.RedisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = com.demo.SpringBootApp.class)
public class TestRedisUtils {

    @Resource
    private RedisService redisService;

    @Test
    public void getRedisData(){
        List<Object> list = new ArrayList<>();
        User user = new User();
        user.setUserId(100000l);
        user.setUserName("zhangsan");
        user.setToken("abcdefg");
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(new Date());
        list.add(user);
        user = new User();
        user.setUserId(200000l);
        user.setUserName("lisi");
        user.setToken("aaabbb");
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(new Date());
        list.add(user);
        //  ===========================================    String 类型操作    =========================================
//         String类型：设置缓存对象到redis
        redisService.setObject("user", user.getUserId().toString(), user);
//        redisService.setObject("", user.getUserId().toString(), user);
        // 从redis中获取缓存对象
        User redisUser = redisService.getObject("user", user.getUserId().toString(), User.class);
        System.out.println("输出redis数据：" + JSONObject.toJSONString(redisUser));

        //  ===========================================    hash 类型操作    =========================================
//      // 添加缓存Hash对象数据
//        redisService.putHashValue("userHash", user.getUserId().toString(), user.getUserId().toString(), user);
        // 根据key值以及传入对象获取缓存hash对象数据
//        User redisUserHash = redisService.getHashValue("userHash", user.getUserId().toString(), user.getUserId().toString(), User.class);
//        System.out.println("输出redis数据：" + JSONObject.toJSONString(redisUserHash));
//      // 根据key值获取缓存hash数据
//        Map<Object, Object> map = redisService.getHashByKey("userHash", user.getUserId().toString());
//        System.out.println("输出redis数据：" + JSONObject.toJSONString(map));
//        map.put("userName", "admin");
//        map.put("userId", 666888l);
//        redisService.putHashValue("userHash", user.getUserId().toString(), map);
//        System.out.println("输出redis数据：" + JSONObject.toJSONString(map));

        //  ===========================================    list 类型操作    =========================================
        // 添加缓存list数据
//        redisService.addList("userList", ((User)list.get(0)).getUserId().toString(), list);
//        redisService.addList("userList", ((User)list.get(1)).getUserId().toString(), list);
        // 获取缓存list指定对象数据
//        List<User> userList = redisService.getListAll("userList", user.getUserId().toString(), User.class);
        // 获取缓存list对象数据
//        List<Object> userList = redisService.getListAll("userList", user.getUserId().toString());
//        System.out.println("输出redis数据：" + JSONObject.toJSONString(userList));
        // 获取缓存list指定下标数据
//        User userInfo = redisService.getListByIndex("userList", user.getUserId().toString(), 1, User.class);
//        System.out.println("输出redis下标为1的数据：" + JSONObject.toJSONString(userInfo));
        // 根据索引获取缓存list数据
//        List<Object> userList = redisService.getListByInterval("userList", user.getUserId().toString(), 0, -1);
//        System.out.println("输出redis指定区间内的所有数据：" + JSONObject.toJSONString(userList));
//      获取缓存list size
//        long ll =  redisService.getListSize("userList", user.getUserId().toString());
//        System.out.println("输出redis list size：" + ll);
        // 删除缓存list类型元素（公共删除方法）
//        redisService.del("userList", user.getUserId().toString());

        //  ===========================================    无序 set 类型操作    =========================================
        // set 类型操作redis
        // 添加set数据到缓存
//        redisService.sSetValue("userSet", user.getUserId().toString(), user);
//        redisService.sSetValue("userSet", user.getUserId().toString(), list);
//        redisService.sSetValue("userSet", user.getUserId().toString(), "A","B","C");

        // 删除set类型数据(字符串)
//        redisService.sDelSet("userSet", user.getUserId().toString(), "B", "C");
        // 获取set类型数据
//        Set<Object> userSet = redisService.sGetValueByKey("userSet", user.getUserId().toString());
        Set<User> userSet = redisService.sGetValueByKey("userSet", user.getUserId().toString(), User.class);
        System.out.println("输出redis数据：" + JSONObject.toJSONString(userSet));

    }
}
