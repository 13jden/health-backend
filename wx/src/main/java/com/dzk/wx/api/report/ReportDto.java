package com.dzk.wx.api.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportDto {

    private Long id;

    private Long childId;

    private String reportContent;

    private Date reportDate;

    private Date createTime;

    private Date updateTime;

}
