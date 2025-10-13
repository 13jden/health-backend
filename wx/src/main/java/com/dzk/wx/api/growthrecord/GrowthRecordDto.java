package com.dzk.wx.api.growthrecord;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Schema(description = "生长记录数据传输对象")
public class GrowthRecordDto {
    
    @Schema(description = "记录ID")
    private Long id;
    
    @Schema(description = "儿童ID")
    private Long childId;
    
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
    @Schema(description = "生长记录输入数据传输对象")
    public static class Input {
        @Schema(description = "儿童ID")
        private Long childId;
        
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
    @Schema(description = "生长记录详情数据传输对象")
    public static class Detail {
        @Schema(description = "记录ID")
        private Long id;
        
        @Schema(description = "儿童ID")
        private Long childId;
        
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
}
