package com.demo.crawler.hexun.common.info;

import com.geccocrawler.gecco.annotation.*;
import com.geccocrawler.gecco.request.HttpRequest;
import com.geccocrawler.gecco.spider.HtmlBean;
import lombok.Data;

/**
 * 和讯网爬虫（股票，保险，理财公用一个爬虫器）
 * 股票——行业市况 链接：https://m.hexun.com/company.html
 * 保险——保险   链接：https://m.hexun.com/insurance.html
 * 理财——理财资讯 理财栏目-https://m.hexun.com/money.html
 * 私募——私募视点 http://funds.hexun.com/pssd/ 
 * 信托——信托要闻 http://trust.hexun.com/xtyw/index.html
 */
@Gecco(pipelines={"hexunCommonNewsInfoPipeline"})
@Data
public class HexunCommonNewsInfo implements HtmlBean {

    @Request
    private HttpRequest request;

    /**
     * @desc 对应文本内容的url
     */
    @Href
    @HtmlField(
            cssPath = "#newsList > ul > li:nth-child(1) > a"
    )
    private String contentUrl;

}
