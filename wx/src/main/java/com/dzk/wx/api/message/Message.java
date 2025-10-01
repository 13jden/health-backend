package com.dzk.wx.api.message;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dzk.common.common.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@TableName("message")
public class Message extends BaseEntity {

    @TableId(type = IdType.AUTO)
    @Schema(description = "消息ID")
    private Long id;

    @Schema(description = "接收者ID")
    private Long receiverId;

    @Schema(description = "接收者角色")
    private String receiverRole;

    @Schema(description = "消息内容")
    private String message;

    @Schema(description = "消息状态")
    private String status;
}
