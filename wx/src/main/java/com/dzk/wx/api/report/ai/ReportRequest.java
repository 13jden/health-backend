package com.dzk.wx.api.report.ai;

import com.dzk.wx.api.child.ChildDto;
import com.dzk.wx.api.dietrecord.DietRecordDto;
import com.dzk.wx.api.exerciserecord.ExerciseRecordDto;

import lombok.Data;

import java.util.List;

@Data
public class ReportRequest {

    private Long childId;

    private ChildDto child;

    private List<ExerciseRecordDto> exerciseRecords;

    private List<DietRecordDto> dietRecords;

}