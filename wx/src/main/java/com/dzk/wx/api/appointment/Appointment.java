package com.dzk.wx.api.appointment;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dzk.common.common.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("appointment")
public class Appointment extends BaseEntity {

    @TableId(type = IdType.AUTO)
    @Schema(description = "预约ID")
    private Long id;

    @Schema(description = "家长ID")
    private Long parentId;

    @Schema(description = "医生ID")
    private Long doctorId;

    @Schema(description = "预约时间")
    private LocalDateTime appointmentTime;

    @Schema(description = "预约状态")
    private String status;
}
