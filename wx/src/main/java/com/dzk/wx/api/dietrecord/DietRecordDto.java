package com.dzk.wx.api.dietrecord;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Schema(description = "饮食记录数据传输对象")
public class DietRecordDto {
    
    @Schema(description = "饮食记录ID")
    private Long id;
    
    @Schema(description = "儿童ID")
    private Long childId;
    
    @Schema(description = "餐次类型")
    private String mealType;
    
    @Schema(description = "食物名称")
    private String foodName;
    
    @Schema(description = "食物分类")
    private String foodCategory;
    
    @Schema(description = "食物分量")
    private BigDecimal quantity;
    
    @Schema(description = "单位")
    private String unit;
    
    @Schema(description = "热量（卡路里）")
    private BigDecimal calories;
    
    @Schema(description = "蛋白质（克）")
    private BigDecimal protein;
    
    @Schema(description = "碳水化合物（克）")
    private BigDecimal carbohydrate;
    
    @Schema(description = "脂肪（克）")
    private BigDecimal fat;
    
    @Schema(description = "膳食纤维（克）")
    private BigDecimal fiber;
    
    @Schema(description = "记录日期")
    private LocalDate recordDate;
    
    @Schema(description = "记录时间")
    private LocalTime recordTime;
    
    @Schema(description = "备注")
    private String notes;

    @Data
    @Schema(description = "饮食记录输入数据传输对象")
    public static class Input {
        @Schema(description = "儿童ID")
        private Long childId;
        
        @Schema(description = "餐次类型")
        private String mealType;
        
        @Schema(description = "食物名称")
        private String foodName;
        
        @Schema(description = "食物分类")
        private String foodCategory;
        
        @Schema(description = "食物分量")
        private BigDecimal quantity;
        
        @Schema(description = "单位")
        private String unit;
        
        @Schema(description = "热量（卡路里）")
        private BigDecimal calories;
        
        @Schema(description = "蛋白质（克）")
        private BigDecimal protein;
        
        @Schema(description = "碳水化合物（克）")
        private BigDecimal carbohydrate;
        
        @Schema(description = "脂肪（克）")
        private BigDecimal fat;
        
        @Schema(description = "膳食纤维（克）")
        private BigDecimal fiber;
        
        @Schema(description = "记录日期")
        private LocalDate recordDate;
        
        @Schema(description = "记录时间")
        private LocalTime recordTime;
        
        @Schema(description = "备注")
        private String notes;
    }

    @Data
    @Schema(description = "饮食记录详情数据传输对象")
    public static class Detail {
        @Schema(description = "饮食记录ID")
        private Long id;
        
        @Schema(description = "儿童ID")
        private Long childId;
        
        @Schema(description = "餐次类型")
        private String mealType;
        
        @Schema(description = "食物名称")
        private String foodName;
        
        @Schema(description = "食物分类")
        private String foodCategory;
        
        @Schema(description = "食物分量")
        private BigDecimal quantity;
        
        @Schema(description = "单位")
        private String unit;
        
        @Schema(description = "热量（卡路里）")
        private BigDecimal calories;
        
        @Schema(description = "蛋白质（克）")
        private BigDecimal protein;
        
        @Schema(description = "碳水化合物（克）")
        private BigDecimal carbohydrate;
        
        @Schema(description = "脂肪（克）")
        private BigDecimal fat;
        
        @Schema(description = "膳食纤维（克）")
        private BigDecimal fiber;
        
        @Schema(description = "记录日期")
        private LocalDate recordDate;
        
        @Schema(description = "记录时间")
        private LocalTime recordTime;
        
        @Schema(description = "备注")
        private String notes;
        
        @Schema(description = "创建时间")
        private String createTime;
        
        @Schema(description = "更新时间")
        private String updateTime;
    }
}
