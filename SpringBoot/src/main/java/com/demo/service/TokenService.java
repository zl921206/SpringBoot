package com.demo.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.demo.constant.Constant;
import com.demo.dto.ResultDto;
import com.demo.entity.User;
import com.demo.exception.CustomException;
import com.demo.utils.IdUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;


/**
 * token服务接口
 *
 * @author zhanglei
 * @date 2020-12-23 14:50
 */
@Service("tokenService")
@Slf4j
public class TokenService {

    /**
     * token失效时间 单位：秒  设置30分钟
     */
    private final static long EXPIRE_TIME = 1800;

    /**
     * 用户token缓存文件夹
     */
    private final static String ACCESS_TOKEN = Constant.USER_TOKEN + Constant.COLON;

    /**
     * 用户userKey缓存文件夹
     */
    private final static String USER_KEY = Constant.USER_KEY + Constant.COLON;


    @Value("${jwt.secret}")
    private String jwtSecret;

    @Resource
    private RedisService redisService;

    /**
     * 将传入参数使用jwt加密算法生成系统全局唯一标识加密串
     * @param value
     * @return
     */
    public String getEncryptStr(String value) {
        String userKey = "";
        userKey = JWT.create().withAudience(value)
                .sign(Algorithm.HMAC256(jwtSecret));
        return userKey;
    }

    /**
     * 获取解密的字符串
     * @param value
     * @return
     */
    public String getDecryptStr(String value) {
        try {
            return JWT.decode(value).getAudience().get(0);
        } catch (JWTDecodeException j) {
            throw new CustomException("userKey解密异常！");
        }
    }

    /**
     * 创建token,存入用户信息
     * @param user
     */
    public ResultDto createToken(User user) {
        // 生成token存入用户信息
        String token = IdUtils.fastUUID();
        log.info("生成token：{}", token);
        redisService.setCacheObject(ACCESS_TOKEN + token, user, EXPIRE_TIME, TimeUnit.SECONDS);
        return ResultDto.success("token创建成功！", token);
    }

    /**
     * 创建userKey
     * @param value
     */
    public ResultDto createUserKey(String value) {
        String userKey = getEncryptStr(value);
        log.info("生成userKey：{}", userKey);
        redisService.setCacheObject( USER_KEY + userKey, userKey, EXPIRE_TIME, TimeUnit.SECONDS);
        return ResultDto.success("userKey创建成功！", userKey);
    }

}
