package com.dzk.wx.api.feedback;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dzk.common.common.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@TableName("feedback")
public class Feedback extends BaseEntity {

    @TableId(type = IdType.AUTO)
    @Schema(description = "反馈ID")
    private Long id;

    @Schema(description = "家长ID")
    private Long parentId;

    @Schema(description = "反馈内容")
    private String content;

    @Schema(description = "回复内容")
    private String reply;
}
