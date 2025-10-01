package com.dzk.wx.api.child;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dzk.common.common.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@TableName("child")
public class Child extends BaseEntity {

    @TableId(type = IdType.AUTO)
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
}
