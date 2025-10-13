package com.dzk.wx.api.exerciserecord;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dzk.common.common.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@TableName("exercise_record")
public class ExerciseRecord extends BaseEntity {

    @TableId(type = IdType.AUTO)
    @Schema(description = "运动记录ID")
    private Long id;

    @Schema(description = "儿童ID")
    private Long childId;

    @Schema(description = "运动类型")
    private String exerciseType;

    @Schema(description = "运动分类")
    private String exerciseCategory;

    @Schema(description = "运动时长（分钟）")
    private Integer duration;

    @Schema(description = "运动强度")
    private String intensity;

    @Schema(description = "消耗热量（卡路里）")
    private BigDecimal caloriesBurned;

    @Schema(description = "运动距离（公里）")
    private BigDecimal distance;

    @Schema(description = "步数")
    private Integer steps;

    @Schema(description = "平均心率")
    private Integer heartRateAvg;

    @Schema(description = "最大心率")
    private Integer heartRateMax;

    @Schema(description = "记录日期")
    private LocalDate recordDate;

    @Schema(description = "开始时间")
    private LocalTime startTime;

    @Schema(description = "结束时间")
    private LocalTime endTime;

    @Schema(description = "备注")
    private String notes;
}
