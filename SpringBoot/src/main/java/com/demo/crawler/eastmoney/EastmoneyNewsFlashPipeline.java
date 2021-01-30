package com.demo.crawler.eastmoney;

import com.geccocrawler.gecco.annotation.PipelineName;
import com.geccocrawler.gecco.pipeline.Pipeline;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@PipelineName("eastmoneyNewsFlashPipeline")
public class EastmoneyNewsFlashPipeline implements Pipeline<EastmoneyNewsFlashDto> {

    @Override
    public void process(EastmoneyNewsFlashDto info) {
        // 获取爬到的文本内容
        String text = info.getText();
        if(StringUtils.isEmpty(text))return;
        // 使用正则表达式获取title
        Matcher matcher = Pattern.compile("【(.*?)】").matcher(text);
        String title = "";
        while(matcher.find()){
            title = matcher.group(1);
        }
        if(StringUtils.isEmpty(title))return;
        // 获取快讯内容
        String content = text.substring(text.indexOf("】") + 1, text.lastIndexOf("。") + 1);
        // 获取快讯发布日期
        String date = info.getDate().substring(info.getDate().lastIndexOf("/") + 1).substring(0, 8);
        date = LocalDate.from(DateTimeFormatter.ofPattern("yyyyMMdd").parse(date)).format(DateTimeFormatter.ofPattern("yyyy-MM-dd "));
        // 获取快讯发布时间
        String time = date + info.getTime();
        System.out.println("输出爬取信息文本：" + text);
        System.out.println("输出爬取信息标题：" + title);
        System.out.println("输出爬取信息内容：" + content);
        System.out.println("输出爬取信息时间：" + time);
        System.out.println("输出爬取信息日期：" + date);
    }

}