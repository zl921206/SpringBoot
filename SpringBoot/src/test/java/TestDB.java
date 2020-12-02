import com.alibaba.fastjson.JSONObject;
import com.demo.entity.SysVersionUpgrade;
import com.demo.service.SysVersionUpgradeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = com.demo.SpringBootApp.class)
public class TestDB {

    @Resource
    private SysVersionUpgradeService sysVersionUpgradeService;

    @Test
    public void queryData(){
        SysVersionUpgrade info = sysVersionUpgradeService.getById(1);
        System.out.println("输出查询结果：" + JSONObject.toJSONString(info));
    }
}
