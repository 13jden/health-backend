package com.dzk.wx.api.report;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportService {

    @Autowired
    private ReportMapper reportMapper;

    @Autowired
    private GenerateReportService generateReportService;

    public String generateReport(Long childId){
        String reportMD = generateReportService.prepareReport(childId);
        Report report = new Report().builder()
                .reportContent(reportMD)
                .childId(childId).build();

        reportMapper.insert(report);
        return reportMD;
    }
}
