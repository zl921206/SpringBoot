package com.demo.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.demo.annotation.TokenCheck;
import com.demo.annotation.SkipTokenCheck;
import com.demo.annotation.UserKeyCheck;
import com.demo.constant.HttpStatus;
import com.demo.entity.User;
import com.demo.exception.CustomException;
import com.demo.request.RequestWrapper;
import com.demo.service.RedisService;
import com.demo.utils.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

import static com.demo.constant.Constant.*;


/**
 * 认证拦截
 *
 * @author zhanglei
 * @date 2020-12-23 14:50
 */
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Resource
    private RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
        String token = httpServletRequest.getHeader(TOKEN_ATTR);// 从 http 请求头中取出 token
        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        /**
         * 检测是否有包含跳过的注解
         */
        if (method.isAnnotationPresent(SkipTokenCheck.class)) {
            SkipTokenCheck passToken = method.getAnnotation(SkipTokenCheck.class);
            if (passToken.required()) {
                return true;
            }
        }
        User user = null;
        /**
         * 检测需要验证token的注解
         */
        if (method.isAnnotationPresent(TokenCheck.class)) {
            TokenCheck userLoginToken = method.getAnnotation(TokenCheck.class);
            if (userLoginToken.required()) {
                // 验证客户端是否有传token
                if (token == null) {
                    throw new CustomException("token不能为空!", HttpStatus.BAD_REQUEST);
                }
                // 查询token是否失效
                user = (User) redisService.getCacheObject(USER_TOKEN + COLON + token);
                if (user == null) {
                    throw new CustomException("无效token，验证失败!", HttpStatus.FORBIDDEN);
                }
            }
        }

        /**
         * 验证jwt生成的加密串
         */
        if (method.isAnnotationPresent(UserKeyCheck.class)) {
            String userKey = "";
            JSONObject jsonObject = JSONObject.parseObject(new RequestWrapper(httpServletRequest).getBody());
            if(null != jsonObject && jsonObject.containsKey(USER_KEY_ATTR)){
                userKey = jsonObject.get(USER_KEY_ATTR).toString();
            }
            UserKeyCheck userKeyCheck = method.getAnnotation(UserKeyCheck.class);
            if (userKeyCheck.required()) {
                if (StringUtils.isEmpty(userKey)) {
                    throw new CustomException("userKey不能为空!", HttpStatus.BAD_REQUEST);
                }
                // 查询缓存中userKey是否失效
                if (StringUtils.isEmpty(redisService.getCacheObject(USER_KEY + COLON + userKey))) {
                    throw new CustomException("userKey已经失效，请重新登录!", HttpStatus.FORBIDDEN);
                }
                // 验证 userKey
                JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(jwtSecret)).build();
                try {
                    jwtVerifier.verify(userKey);
                } catch (JWTVerificationException e) {
                    throw new CustomException("userKey验证失败!", HttpStatus.UNAUTHORIZED);
                }
                // 将post请求中的参数拿到并塞入request中的attribute属性
                httpServletRequest.setAttribute(USER_KEY_ATTR, userKey);
            }
        }
        // 因为可能存在多注解验证，所以在这里统一进行放行操作
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
