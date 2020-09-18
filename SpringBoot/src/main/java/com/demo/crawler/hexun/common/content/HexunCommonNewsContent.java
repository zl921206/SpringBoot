package com.demo.crawler.hexun.common.content;

import com.geccocrawler.gecco.annotation.*;
import com.geccocrawler.gecco.request.HttpRequest;
import com.geccocrawler.gecco.spider.HtmlBean;
import lombok.Data;

@Gecco(pipelines={"hexunCommonNewsContentPipeline"})
@Data
public class HexunCommonNewsContent implements HtmlBean {

    @Request
    private HttpRequest request;

    /**
     * 新闻内容
     */
    @Html
    @HtmlField(cssPath = "body > div.main > article > article")
    private String content;

    /**
     * 新闻时间
     */
    @Text
    @HtmlField(cssPath = "body > div.main > article > div.dettime > span:nth-child(1)")
    private String time;

    /**
     * 新闻标题
     */
    @Text
    @HtmlField(cssPath = "body > div.main > article > h2")
    private String title;

}
