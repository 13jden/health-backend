package com.dzk.wx.api.report;


import com.dzk.common.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

@RestController("report")
public class ReportController {
    @Autowired
    private ReportService reportService;

    @PostMapping("generate/{childId}")
    public Result<String> generateReport(@PathVariable Long childId){
        return Result.success(reportService.generateReport(childId));
    }

}
