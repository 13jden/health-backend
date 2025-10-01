 package com.dzk.wx.api.log;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dzk.common.common.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("log")
public class Log extends BaseEntity {

    @TableId(type = IdType.AUTO)
    @Schema(description = "日志ID")
    private Long id;

    @Schema(description = "操作动作")
    private String action;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "操作时间")
    private LocalDateTime actionTime;

    @Schema(description = "操作详情")
    private String details;
}