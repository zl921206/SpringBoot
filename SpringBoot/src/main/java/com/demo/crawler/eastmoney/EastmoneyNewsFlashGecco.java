package com.demo.crawler.eastmoney;

import com.geccocrawler.gecco.GeccoEngine;
import com.geccocrawler.gecco.request.HttpGetRequest;
import com.geccocrawler.gecco.request.HttpRequest;

public class EastmoneyNewsFlashGecco {

    /**
     * 东方财富网快讯地址
     */
    public static final String url = "https://kuaixun.eastmoney.com/";

    public static void main(String[] args) {
        //  开始抓取
        HttpRequest request = new HttpGetRequest(url);
        request.setCharset("GBK");
        GeccoEngine.create()
                .classpath("com.demo.crawler")
                .start(request)
                .run();
    }
}
