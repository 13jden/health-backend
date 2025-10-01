package com.dzk.wx.api.predictionresult;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dzk.common.common.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDate;

@Data
@TableName("prediction_result")
public class PredictionResult extends BaseEntity {

    @TableId(type = IdType.AUTO)
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
} 