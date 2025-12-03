package com.dzk.wx.api.report;


import com.dzk.common.exception.BusinessException;
import com.dzk.wx.api.child.Child;
import com.dzk.wx.api.child.ChildMapper;
import com.dzk.wx.api.report.ai.GenerateReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportService {

    @Autowired
    private ReportMapper reportMapper;

    @Autowired
    private ChildMapper childMapper;

    @Autowired
    private GenerateReportService generateReportService;


    @Transactional
    public String generateReport(Long childId){
        Child child = childMapper.getChildById(childId);
        if(child==null){
            throw new BusinessException("不存在患者");
        }
        String reportMD = generateReportService.prepareReport(childId);
        Report report = new Report().builder()
                .reportContent(reportMD)
                .childId(childId)
                .reportDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .build();

        reportMapper.insert(report);
        return reportMD;
    }

    public List<ReportDto> getUserReport(Long childId) {
        List<Report> reports = reportMapper.selectByChildId(childId);
        List<ReportDto> reportDtos = new ArrayList<>();
        for (Report report : reports){
            ReportDto reportDto = ReportDto
                    .builder()
                    .reportDate(report.getReportDate())
                    .childId(childId)
                    .reportContent(report.getReportContent())
                    .build();
            reportDtos.add(reportDto);
        }
        return reportDtos;
    }
}
