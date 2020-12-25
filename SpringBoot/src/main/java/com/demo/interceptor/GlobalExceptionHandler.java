package com.demo.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.demo.constant.HttpStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 认证拦截
 * @author zhanglei
 * @date 2020-12-23 14:50
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Object handleException(Exception e) {
        e.printStackTrace();
        log.error("发生系统异常：{}", e);
        String msg = e.getMessage();
        if (msg == null || msg.equals("")) {
            msg = "系统异常！";
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", HttpStatus.ERROR);
        jsonObject.put("message", msg);
        return jsonObject;
    }
}
