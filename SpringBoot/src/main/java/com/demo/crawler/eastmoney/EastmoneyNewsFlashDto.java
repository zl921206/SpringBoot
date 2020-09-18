package com.demo.crawler.eastmoney;

import com.geccocrawler.gecco.annotation.Gecco;
import com.geccocrawler.gecco.annotation.Href;
import com.geccocrawler.gecco.annotation.HtmlField;
import com.geccocrawler.gecco.annotation.Text;
import com.geccocrawler.gecco.spider.HtmlBean;

/**
 * 东方财富网快讯爬虫
 */
@Gecco(matchUrl="https://kuaixun.eastmoney.com/", pipelines={"eastmoneyNewsFlashPipeline"})
public class EastmoneyNewsFlashDto implements HtmlBean {

    @Text
    @HtmlField(cssPath="#livenews-list > div.livenews-media > div.media-content > span.time")
    private String time;

    @Text
    @HtmlField(cssPath = "#livenews-list > div.livenews-media > div.media-content > h2 > a")
    private String text;

    @Href
    @HtmlField(cssPath="#livenews-list > div.livenews-media > div.media-content > h2 > a")
    private String date;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
