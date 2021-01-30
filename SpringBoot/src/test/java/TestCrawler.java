import com.demo.crawler.eastmoney.EastmoneyNewsFlashPipeline;
import com.demo.crawler.hexun.common.info.HexunCommonNewsInfoPipeline;
import com.geccocrawler.gecco.GeccoEngine;
import com.geccocrawler.gecco.pipeline.Pipeline;
import com.geccocrawler.gecco.pipeline.PipelineFactory;
import com.geccocrawler.gecco.request.HttpGetRequest;
import com.geccocrawler.gecco.spider.SpiderBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = com.demo.SpringBootApp.class)
public class TestCrawler {

    /**
     * 和讯网资讯地址
     */
    public static final String url_insurance = "https://m.hexun.com/insurance.html";
    public static final String url_stock = "https://m.hexun.com/company.html";
    public static final String url_money = "https://m.hexun.com/money.html";
    public static final String url_funds = "https://m.hexun.com/funds.html";

    @Resource
    private HexunCommonNewsInfoPipeline hexunCommonNewsInfoPipeline;

    /**
     * 和讯网爬虫（股票，保险，理财公用一个爬虫器）
     */
    @Test
    public void crawlerHexunStockTest() {
        List<String> urlList = new ArrayList<>();
        urlList.add(url_insurance);
        urlList.add(url_stock);
        urlList.add(url_money);
        urlList.add(url_funds);
        int size = urlList.size();
        for (int i = 0; i < size; i++) {
            GeccoEngine.create()
                    .classpath("com.demo.crawler.hexun.common.info")
                    .start(new HttpGetRequest(urlList.get(i)))
                    .thread(1)
                    .pipelineFactory(new PipelineFactory() {
                        @Override
                        public Pipeline<? extends SpiderBean> getPipeline(String name) {
                            return hexunCommonNewsInfoPipeline;
                        }
                    })
                    .loop(false)
                    .mobile(false)
                    .run();
        }
    }

    /**
     * 东方财富网资讯地址
     */
    public static final String url_eastmoney = "https://kuaixun.eastmoney.com/";

    @Resource
    private EastmoneyNewsFlashPipeline eastmoneyNewsFlashPipeline;

    /**
     * 东方财富网爬虫
     */
    @Test
    public void crawlerEastmoneyStockTest() {
        GeccoEngine.create()
                .classpath("com.demo.crawler.eastmoney")
                .start(new HttpGetRequest(url_eastmoney))
                .thread(1)
                .pipelineFactory(new PipelineFactory() {
                    @Override
                    public Pipeline<? extends SpiderBean> getPipeline(String name) {
                        return eastmoneyNewsFlashPipeline;
                    }
                })
                .loop(false)
                .mobile(false)
                .run();
    }
}
