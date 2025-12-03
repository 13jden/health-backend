package com.dzk.wx.api.report;


import com.dzk.common.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("report")
public class ReportController {
    @Autowired
    private ReportService reportService;

    @PostMapping("generate/{childId}")
    public Result<String> generateReport(@PathVariable Long childId){
        return Result.success(reportService.generateReport(childId));
    }


    @GetMapping("list/{childId}")
    public Result<List<ReportDto>> getUserReport(@PathVariable Long childId){
        return Result.success(reportService.getUserReport(childId));
    }

}
