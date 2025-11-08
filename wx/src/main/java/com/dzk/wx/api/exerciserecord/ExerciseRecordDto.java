package com.dzk.wx.api.exerciserecord;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Schema(description = "运动记录数据传输对象")
public class ExerciseRecordDto {
    
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

    @Data
    @Schema(description = "运动记录输入数据传输对象")
    public static class Input {
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

    @Data
    @Schema(description = "运动记录快速输入数据传输对象")
    public static class QuickInput {
        @Schema(description = "儿童ID")
        private Long childId;

        @Schema(description = "输入内容")
        private String content;
    }

    @Data
    @Schema(description = "运动记录详情数据传输对象")
    public static class Detail {
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
        
        @Schema(description = "创建时间")
        private String createTime;
        
        @Schema(description = "更新时间")
        private String updateTime;
    }
}
