package com.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 用户类
 */
@Data
public class User {

    /**
     * 用户token
     */
    @NotEmpty(message = "用户token不能为空")
    private String token;

    /**
     * 用户姓名
     */
    @NotEmpty(message = "用户姓名不能为空")
    private String userName;

    /**
     * 用户ID
     */
    @NotNull(message = "userId不能为空")
    @Min(value = 1, message = "用户ID不能小于1")
    private Long userId;

    /**
     * 创建时间
     */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

}
