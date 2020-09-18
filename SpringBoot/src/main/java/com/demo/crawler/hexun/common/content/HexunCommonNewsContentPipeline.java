package com.demo.crawler.hexun.common.content;

import com.geccocrawler.gecco.annotation.PipelineName;
import com.geccocrawler.gecco.pipeline.Pipeline;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@PipelineName("hexunCommonNewsContentPipeline")
@Slf4j
public class HexunCommonNewsContentPipeline implements Pipeline<HexunCommonNewsContent> {

    @Override
    public void process(HexunCommonNewsContent bean) {
        log.info("开始爬取和讯网股票资讯文本内容信息......");
        // 根据新闻contentUrl爬取content
        if (bean != null) {
            String url = bean.getRequest().getUrl();
            String title = bean.getTitle();
            String content = bean.getContent();
            String time = LocalDate.now().format(DateTimeFormatter.ofPattern(bean.getTime() + ":00"));
            System.out.println("输出和讯网股票资讯url：" + url);
            System.out.println("输出和讯网股票资讯Title：" + title);
            System.out.println("输出和讯网股票资讯content：" + content);
            System.out.println("输出和讯网股票资讯发布时间time：" + time);
        }
    }

}