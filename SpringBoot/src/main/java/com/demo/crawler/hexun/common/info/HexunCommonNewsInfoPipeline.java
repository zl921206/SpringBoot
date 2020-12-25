package com.demo.crawler.hexun.common.info;

import com.demo.crawler.hexun.common.content.HexunCommonNewsContentPipeline;
import com.demo.utils.StringUtils;
import com.geccocrawler.gecco.GeccoEngine;
import com.geccocrawler.gecco.annotation.PipelineName;
import com.geccocrawler.gecco.pipeline.Pipeline;
import com.geccocrawler.gecco.pipeline.PipelineFactory;
import com.geccocrawler.gecco.request.HttpGetRequest;
import com.geccocrawler.gecco.spider.SpiderBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@PipelineName("hexunCommonNewsInfoPipeline")
@Slf4j
public class HexunCommonNewsInfoPipeline implements Pipeline<HexunCommonNewsInfo> {

    @Resource
    HexunCommonNewsContentPipeline hexunCommonNewsContentPipeline;

    @Override
    public void process(HexunCommonNewsInfo bean) {
        log.info("开始爬取和讯网股票信息......");
        String url = bean.getContentUrl();
        if (!StringUtils.isEmpty(url)) {
            GeccoEngine.create()
                    .classpath("com.demo.crawler.hexun.common.content")
                    .start(new HttpGetRequest(url))
                    .thread(1)
                    .pipelineFactory(new PipelineFactory() {
                        @Override
                        public Pipeline<? extends SpiderBean> getPipeline(String name) {
                            return hexunCommonNewsContentPipeline;
                        }
                    })
                    .loop(false)
                    .mobile(false)
                    .run();
        }
    }
}