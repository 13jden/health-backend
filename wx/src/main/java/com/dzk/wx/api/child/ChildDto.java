package com.dzk.wx.api.child;

import java.util.List;

import com.dzk.wx.api.growthrecord.GrowthRecordDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Schema(description = "儿童数据传输对象")
public class ChildDto {
    
    @Schema(description = "儿童ID")
    private Long id;
    
    @Schema(description = "家长ID")
    private Long parentId;
    
    @Schema(description = "儿童姓名")
    private String name;
    
    @Schema(description = "性别")
    private String gender;
    
    @Schema(description = "出生日期")
    private LocalDate birthdate;
    
    @Schema(description = "身高(cm)")
    private BigDecimal height;
    
    @Schema(description = "体重(kg)")
    private BigDecimal weight;
    
    @Schema(description = "BMI指数")
    private BigDecimal bmi;
    
    @Schema(description = "骨龄")
    private BigDecimal boneAge;
    
    @Schema(description = "检测日期")
    private LocalDate testDate;

    @Data
    @Schema(description = "儿童输入数据传输对象")
    public static class Input {
        @Schema(description = "家长ID")
        private Long parentId;
        
        @Schema(description = "儿童姓名")
        private String name;
        
        @Schema(description = "性别")
        private String gender;
        
        @Schema(description = "出生日期")
        private LocalDate birthdate;
        
        @Schema(description = "身高(cm)")
        private BigDecimal height;
        
        @Schema(description = "体重(kg)")
        private BigDecimal weight;
        
        @Schema(description = "BMI指数")
        private BigDecimal bmi;
        
        @Schema(description = "骨龄")
        private BigDecimal boneAge;
        
        @Schema(description = "检测日期")
        private LocalDate testDate;
    }

    @Data
    @Schema(description = "儿童详情数据传输对象")
    public static class Detail {
        @Schema(description = "儿童ID")
        private Long id;
        
        @Schema(description = "家长ID")
        private Long parentId;
        
        @Schema(description = "儿童姓名")
        private String name;
        
        @Schema(description = "性别")
        private String gender;
        
        @Schema(description = "出生日期")
        private LocalDate birthdate;
        
        @Schema(description = "身高(cm)")
        private BigDecimal height;
        
        @Schema(description = "体重(kg)")
        private BigDecimal weight;
        
        @Schema(description = "BMI指数")
        private BigDecimal bmi;
        
        @Schema(description = "骨龄")
        private BigDecimal boneAge;
        
        @Schema(description = "检测日期")
        private LocalDate testDate;
        
        @Schema(description = "创建时间")
        private String createTime;
        
        @Schema(description = "更新时间")
        private String updateTime;
    }

        @Data
    @Schema(description = "儿童基础信息(模型对话使用)")
    public static class Base {
        @Schema(description = "儿童信息")
        private ChildDto child;

        @Schema(description = "最近生长数据")
        private List<GrowthRecordDto> growthRecord;
        

    }
} 