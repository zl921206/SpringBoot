package com.demo.controller;

import com.demo.annotation.TokenCheck;
import com.demo.annotation.UserKeyCheck;
import com.demo.dto.ResultDto;
import com.demo.entity.User;
import com.demo.service.TokenService;
import com.demo.utils.HttpUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.demo.constant.Constant.USER_KEY_ATTR;

/**
 * <p>
 * 系统版本升级信息表 前端控制器
 * </p>
 *
 * @author zhanglei
 * @since 2020-12-02
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    TokenService tokenService;

    /**
     * 接口测试
     * @param request
     * @param response
     * @throws IOException
     * 请求方式get，可以无参
     * TODO 注意：请求Headers中必须携带有效token
     */
    // 开启token验证
    @TokenCheck
    @GetMapping("/test")
    public void test(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.getWriter().write(HttpUtils.getBodyString(request));
    }

    /**
     * 保存，即生成token
     * @throws IOException
     * 请求方式get，可以无参
     * 返回参数：{
     *     "msg": "token创建成功！",
     *     "code": 200,
     *     "data": "5171f57a-1c49-440b-ba99-799ecd950c72"
     * }
     */
    @GetMapping("/saveToken")
    public ResultDto saveToken() {
        return tokenService.createToken(new User());
    }

    /**
     * 保存userKey：实际上就是一个jwt加密算法生成的字符串
     * @throws IOException
     * 请求方式get，可以无参
     * 返回参数：{
     *     "msg": "userKey创建成功！",
     *     "code": 200,
     *     "data": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIxMjM0NTYifQ.-g5MTRAmQU51RdN1fetsoWBXLvjxdeaHrQLk25hI0_A"
     * }
     */
    @GetMapping("/saveUserKey")
    public ResultDto saveUserKey() {
        return tokenService.createUserKey("123456");
    }

    /**
     * 获取解密的userKey
     * @param request
     * @return
     * 请求参数实例：{
     *     "userId":100000,
     *     "userName":"zhangsan",
     *     "userKey":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIxMjM0NTYifQ.-g5MTRAmQU51RdN1fetsoWBXLvjxdeaHrQLk25hI0_A"
     * }
     * TODO 注意：请求参数中必须带有userKey，以及token
     * 响应参数：{
     *     "msg": "获取userKey成功！",
     *     "code": 200,
     *     "data": "123456"
     * }
     */
    // 开启token验证
    @TokenCheck
    // 开启userKey验证
    @UserKeyCheck
    @PostMapping("/getUserKey")
    public ResultDto getUserKey(HttpServletRequest request){
        String userKey = request.getAttribute(USER_KEY_ATTR).toString();
        return ResultDto.success("获取userKey成功！", tokenService.getDecryptStr(userKey));
    }

}

