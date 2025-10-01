package com.dzk.wx.api.predictionresult;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDate;

@Data
@Schema(description = "预测结果数据传输对象")
public class PredictionResultDto {
    
    @Schema(description = "预测结果ID")
    private Long id;
    
    @Schema(description = "儿童ID")
    private Long childId;
    
    @Schema(description = "风险等级")
    private String riskLevel;
    
    @Schema(description = "预测日期")
    private LocalDate predictionDate;
    
    @Schema(description = "模型版本")
    private String modelVersion;

    @Data
    @Schema(description = "预测结果输入数据传输对象")
    public static class Input {
        @Schema(description = "儿童ID")
        private Long childId;
        
        @Schema(description = "风险等级")
        private String riskLevel;
        
        @Schema(description = "预测日期")
        private LocalDate predictionDate;
        
        @Schema(description = "模型版本")
        private String modelVersion;
    }

    @Data
    @Schema(description = "预测结果详情数据传输对象")
    public static class Detail {
        @Schema(description = "预测结果ID")
        private Long id;
        
        @Schema(description = "儿童ID")
        private Long childId;
        
        @Schema(description = "风险等级")
        private String riskLevel;
        
        @Schema(description = "预测日期")
        private LocalDate predictionDate;
        
        @Schema(description = "模型版本")
        private String modelVersion;
        
        @Schema(description = "创建时间")
        private String createTime;
        
        @Schema(description = "更新时间")
        private String updateTime;
    }
} 