package com.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 系统版本升级信息表
 * </p>
 *
 * @author zhanglei
 * @since 2020-12-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysVersionUpgrade implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * APP来源（1：安卓，2：IOS，3：其他）
     */
    private Integer appSource;

    /**
     * 最新版本号（显示）
     */
    private String newestVersionDisplay;

    /**
     * 最新版本号（用于判断是否需要升级到最新版本）
     */
    private Integer newestVersionDecide;

    /**
     * 最新版本下载资源链接
     */
    private String downloadUrl;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
