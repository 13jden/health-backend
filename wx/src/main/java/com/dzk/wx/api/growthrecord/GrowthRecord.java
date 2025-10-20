package com.dzk.wx.api.growthrecord;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dzk.common.common.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@TableName("growth_record")
public class GrowthRecord extends BaseEntity {

    @TableId(type = IdType.AUTO)
    @Schema(description = "记录ID")
    private Long id;

    @TableField("child_id")
    @Schema(description = "儿童ID")
    private Long childId;

    @Schema(description = "身高(cm)")
    private BigDecimal height;

    @Schema(description = "体重(kg)")
    private BigDecimal weight;

    @TableField("bmi")
    @Schema(description = "BMI指数")
    private BigDecimal bmi;

    @TableField("bone_age")
    @Schema(description = "骨龄")
    private BigDecimal boneAge;

    @TableField("test_date")
    @Schema(description = "检测日期")
    private LocalDate testDate;
}
